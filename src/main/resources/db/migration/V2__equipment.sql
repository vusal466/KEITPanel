-- =====================================================================
-- V2: Avadanlıq + təhkim (assignment) tarixçəsi
-- =====================================================================

CREATE TYPE equipment_type AS ENUM (
    'DESKTOP',        -- sistem bloku
    'LAPTOP',
    'MONITOR',
    'KEYBOARD',
    'MOUSE',
    'PRINTER',
    'SCANNER',
    'MFP',            -- çoxfunksiyalı
    'ROUTER',
    'SWITCH',
    'ACCESS_POINT',
    'FIREWALL',
    'SERVER',
    'UPS',
    'TV',
    'PROJECTOR',
    'IP_PHONE',
    'CARTRIDGE',      -- kartric
    'OTHER'
);

CREATE TYPE equipment_status AS ENUM (
    'IN_USE',         -- istifadədə
    'IN_STOCK',       -- anbarda, boş
    'IN_REPAIR',      -- təmirdə
    'RESERVED',       -- ayrılıb, hələ verilməyib
    'WRITTEN_OFF',    -- silinib
    'LOST'            -- itkin
);

CREATE TYPE data_source AS ENUM (
    'MANUAL',         -- operator əl ilə daxil edib
    'AGENT',          -- kompüterdəki agent göndərib
    'IMPORT'          -- CSV / Excel importu
);


-- ---------------------------------------------------------------------
-- AVADANLIQ
-- Tək cədvəl + type enum + JSONB specs.
-- Səbəb: yeni növ avadanlıq (skaner, NAS) əlavə etmək üçün
-- kod və ya sxem dəyişikliyi lazım olmur.
-- ---------------------------------------------------------------------
CREATE TABLE equipment (
    id              BIGSERIAL PRIMARY KEY,
    inventory_no    VARCHAR(50) UNIQUE,           -- şirkətin öz inventar nömrəsi
    serial_no       VARCHAR(120),                 -- istehsalçı seriya nömrəsi
    type            equipment_type   NOT NULL,
    status          equipment_status NOT NULL DEFAULT 'IN_STOCK',
    brand           VARCHAR(80),                  -- Dell, HP, Canon
    model           VARCHAR(120),

    branch_id       BIGINT NOT NULL REFERENCES branch(id) ON DELETE RESTRICT,
    department_id   BIGINT REFERENCES department(id) ON DELETE SET NULL,
    room            VARCHAR(50),                  -- otaq / mərtəbə

    -- Texniki parametrlər. Növə görə fərqli açarlar:
    --   DESKTOP/LAPTOP: {"cpu":"i5-10400","ramGb":16,"disks":[{"type":"SSD","sizeGb":512}],
    --                    "os":"Windows 11 Pro","osBuild":"22631","hostname":"BAK-PC-014",
    --                    "macAddresses":["A4:BB:.."],"ipAddress":"10.0.14.22"}
    --   PRINTER:        {"connection":"NETWORK","ipAddress":"10.0.14.90","colorSupport":true}
    --   MONITOR:        {"sizeInch":24,"resolution":"1920x1080","panel":"IPS"}
    --   CARTRIDGE:      {"compatibleModels":["Canon 725"],"pageYield":1600}
    specs           JSONB NOT NULL DEFAULT '{}',

    purchase_date   DATE,
    warranty_until  DATE,
    price           NUMERIC(12,2),
    supplier        VARCHAR(150),
    note            TEXT,

    -- Bu sətri kim yaratdı/yenilədi: agent, yoxsa insan?
    source          data_source NOT NULL DEFAULT 'MANUAL',
    -- Agent bu sətri yeniləyə bilməz (operator əl ilə düzəldib, üstündən yazma)
    manual_lock     BOOLEAN NOT NULL DEFAULT false,
    last_seen_at    TIMESTAMPTZ,                  -- agent sonuncu dəfə nə vaxt xəbər verdi

    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT chk_equipment_warranty
        CHECK (warranty_until IS NULL OR purchase_date IS NULL
               OR warranty_until >= purchase_date)
);

-- Eyni seriya nömrəsi iki dəfə qeydiyyata düşməsin (NULL-lar istisna)
CREATE UNIQUE INDEX uq_equipment_serial
    ON equipment(serial_no) WHERE serial_no IS NOT NULL;

CREATE INDEX idx_equipment_branch_status ON equipment(branch_id, status);
CREATE INDEX idx_equipment_type          ON equipment(type);
CREATE INDEX idx_equipment_active        ON equipment(branch_id, type)
    WHERE status IN ('IN_USE', 'IN_STOCK');
-- JSONB üzrə axtarış: specs->>'hostname' = 'BAK-PC-014'
CREATE INDEX idx_equipment_specs         ON equipment USING GIN (specs);

CREATE TRIGGER trg_equipment_updated
    BEFORE UPDATE ON equipment
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- ---------------------------------------------------------------------
-- TƏHKİM TARİXÇƏSİ
-- equipment.employee_id qoymuruq — tarixçə itər.
-- "2024-cü ildə bu noutbuk kimdə idi?" sualına cavab verə bilməliyik.
-- ---------------------------------------------------------------------
CREATE TABLE equipment_assignment (
    id              BIGSERIAL PRIMARY KEY,
    equipment_id    BIGINT NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
    employee_id     BIGINT REFERENCES employee(id) ON DELETE SET NULL,
    position_id     BIGINT REFERENCES position(id) ON DELETE SET NULL,
    branch_id       BIGINT NOT NULL REFERENCES branch(id),

    assigned_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    returned_at     TIMESTAMPTZ,
    assigned_by     VARCHAR(100),                 -- kim təhkim etdi
    handover_doc_no VARCHAR(50),                  -- təhvil-təslim aktı nömrəsi
    note            TEXT,

    CONSTRAINT chk_assignment_period
        CHECK (returned_at IS NULL OR returned_at >= assigned_at)
);

-- Bir avadanlığın eyni anda YALNIZ bir aktiv təhkimi ola bilər
CREATE UNIQUE INDEX uq_assignment_active
    ON equipment_assignment(equipment_id) WHERE returned_at IS NULL;

CREATE INDEX idx_assignment_employee ON equipment_assignment(employee_id);
CREATE INDEX idx_assignment_equipment ON equipment_assignment(equipment_id, assigned_at DESC);


-- ---------------------------------------------------------------------
-- AVADANLIQ ƏLAQƏSİ (monitor -> hansı sistem blokuna qoşulub)
-- ---------------------------------------------------------------------
CREATE TABLE equipment_link (
    id            BIGSERIAL PRIMARY KEY,
    parent_id     BIGINT NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
    child_id      BIGINT NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT uq_equipment_link UNIQUE (parent_id, child_id),
    CONSTRAINT chk_no_self_link  CHECK (parent_id <> child_id)
);

CREATE INDEX idx_equipment_link_child ON equipment_link(child_id);


-- ---------------------------------------------------------------------
-- SƏRVİS / TƏMİR JURNALI
-- ---------------------------------------------------------------------
CREATE TABLE maintenance_record (
    id            BIGSERIAL PRIMARY KEY,
    equipment_id  BIGINT NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
    started_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    finished_at   TIMESTAMPTZ,
    problem       TEXT NOT NULL,
    solution      TEXT,
    cost          NUMERIC(12,2),
    vendor        VARCHAR(150),
    created_by    VARCHAR(100),

    CONSTRAINT chk_maintenance_period
        CHECK (finished_at IS NULL OR finished_at >= started_at)
);

CREATE INDEX idx_maintenance_equipment ON maintenance_record(equipment_id, started_at DESC);
CREATE INDEX idx_maintenance_open      ON maintenance_record(equipment_id)
    WHERE finished_at IS NULL;
