-- =====================================================================
-- V4: Agent (kompüterlərə quraşdırılan proqram) + skan nəticələri
--
-- ƏSAS PRİNSİP: Agent birbaşa `equipment` cədvəlini yeniləmir.
-- Xam data `inventory_scan`-a düşür, sonra reconcile prosesi
-- fərqləri hesablayır və qaydalara görə tətbiq edir.
-- Bu olmasa bir səhv skan bütün inventarı korlaya bilər.
-- =====================================================================

CREATE TYPE agent_status AS ENUM ('ONLINE', 'STALE', 'OFFLINE', 'REVOKED');

CREATE TYPE scan_status AS ENUM ('RECEIVED', 'PROCESSED', 'FAILED', 'IGNORED');

CREATE TYPE diff_action AS ENUM ('AUTO_APPLIED', 'PENDING_REVIEW', 'APPROVED', 'REJECTED');


-- ---------------------------------------------------------------------
-- AGENT (bir kompüter = bir agent)
-- ---------------------------------------------------------------------
CREATE TABLE device_agent (
    id              BIGSERIAL PRIMARY KEY,
    -- Maşın dəyişməz kimliyi: BIOS UUID (Win32_ComputerSystemProduct.UUID)
    machine_uuid    UUID NOT NULL UNIQUE,
    hostname        VARCHAR(120) NOT NULL,
    equipment_id    BIGINT REFERENCES equipment(id) ON DELETE SET NULL,
    branch_id       BIGINT REFERENCES branch(id) ON DELETE SET NULL,

    api_key_hash    VARCHAR(255) NOT NULL,        -- BCrypt. Açıq açar saxlanmır.
    agent_version   VARCHAR(20),
    os_version      VARCHAR(100),
    status          agent_status NOT NULL DEFAULT 'ONLINE',
    last_seen_at    TIMESTAMPTZ,
    enrolled_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    revoked_at      TIMESTAMPTZ,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_agent_equipment ON device_agent(equipment_id);
CREATE INDEX idx_agent_stale     ON device_agent(last_seen_at)
    WHERE status IN ('ONLINE', 'STALE');

CREATE TRIGGER trg_device_agent_updated
    BEFORE UPDATE ON device_agent
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- ---------------------------------------------------------------------
-- XAM SKAN (agent-in göndərdiyi JSON olduğu kimi)
-- Heç vaxt UPDATE edilmir — append-only jurnal.
-- ---------------------------------------------------------------------
CREATE TABLE inventory_scan (
    id             BIGSERIAL PRIMARY KEY,
    agent_id       BIGINT NOT NULL REFERENCES device_agent(id) ON DELETE CASCADE,
    scanned_at     TIMESTAMPTZ NOT NULL,          -- agent-in vaxtı
    received_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    status         scan_status NOT NULL DEFAULT 'RECEIVED',
    payload_hash   VARCHAR(64) NOT NULL,          -- SHA-256, dublikat skanı atmaq üçün

    -- Tam payload:
    -- { "hardware": {...}, "os": {...}, "network": {...},
    --   "outlook": { "profiles":[{"displayName":"...","smtpAddress":"...",
    --                             "signatures":[{"name":"...","html":"..."}]}] },
    --   "loggedOnUser": "DOMAIN\\vcafarli",
    --   "peripherals": [{"type":"MONITOR","serial":"...","model":"..."}] }
    payload        JSONB NOT NULL,
    error_message  TEXT
);

CREATE INDEX idx_scan_agent      ON inventory_scan(agent_id, scanned_at DESC);
CREATE INDEX idx_scan_unprocessed ON inventory_scan(received_at)
    WHERE status = 'RECEIVED';
CREATE UNIQUE INDEX uq_scan_dedupe ON inventory_scan(agent_id, payload_hash);


-- ---------------------------------------------------------------------
-- FƏRQ JURNALI (nə dəyişdi, tətbiq olundumu)
-- IT şöbəsi bunu görür: "PC-014-də RAM 16GB -> 8GB oldu"
-- ---------------------------------------------------------------------
CREATE TABLE inventory_diff (
    id             BIGSERIAL PRIMARY KEY,
    scan_id        BIGINT NOT NULL REFERENCES inventory_scan(id) ON DELETE CASCADE,
    equipment_id   BIGINT REFERENCES equipment(id) ON DELETE CASCADE,
    field_path     VARCHAR(120) NOT NULL,         -- "specs.ramGb", "specs.os"
    old_value      TEXT,
    new_value      TEXT,
    action         diff_action NOT NULL DEFAULT 'PENDING_REVIEW',
    reviewed_by    VARCHAR(100),
    reviewed_at    TIMESTAMPTZ,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_diff_pending   ON inventory_diff(created_at DESC)
    WHERE action = 'PENDING_REVIEW';
CREATE INDEX idx_diff_equipment ON inventory_diff(equipment_id, created_at DESC);


-- ---------------------------------------------------------------------
-- HANSI SAHƏNİ AGENT YENİLƏYƏ BİLƏR
-- Konfiqurasiya cədvəli: hard-code etmə, DB-dən oxu.
-- ---------------------------------------------------------------------
CREATE TABLE agent_field_policy (
    id            BIGSERIAL PRIMARY KEY,
    field_path    VARCHAR(120) NOT NULL UNIQUE,
    auto_apply    BOOLEAN NOT NULL DEFAULT false,  -- true = review-suz yaz
    description   TEXT
);

INSERT INTO agent_field_policy (field_path, auto_apply, description) VALUES
    ('specs.os',            true,  'ƏS versiyası — təhlükəsiz, avtomatik'),
    ('specs.osBuild',       true,  'Build nömrəsi'),
    ('specs.hostname',      true,  'Kompüter adı'),
    ('specs.ipAddress',     true,  'IP ünvanı — tez-tez dəyişir'),
    ('specs.macAddresses',  true,  'MAC ünvanları'),
    ('specs.diskFreeGb',    true,  'Boş disk sahəsi'),
    ('specs.cpu',           false, 'CPU dəyişibsə — ya təmir, ya səhv. Yoxla.'),
    ('specs.ramGb',         false, 'RAM azalıbsa — oğurluq riski. Mütləq yoxla.'),
    ('specs.disks',         false, 'Disk dəyişikliyi — yoxla'),
    ('serial_no',           false, 'Seriya nömrəsi heç vaxt avtomatik dəyişməməlidir'),
    ('branch_id',           false, 'Filial dəyişikliyi yalnız əl ilə'),
    ('status',              false, 'Status yalnız əl ilə');
