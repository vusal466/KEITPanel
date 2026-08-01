-- =====================================================================
-- V1: Təşkilati struktur
-- Branch -> Department -> Position -> Employee
-- =====================================================================

-- updated_at avtomatik yenilənməsi üçün ümumi trigger funksiyası
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- ---------------------------------------------------------------------
-- FİLİAL
-- ---------------------------------------------------------------------
CREATE TABLE branch (
    id           BIGSERIAL PRIMARY KEY,
    code         VARCHAR(20)  NOT NULL UNIQUE,   -- "BAK-01", "GNC-02"
    name         VARCHAR(150) NOT NULL,
    address      TEXT,
    city         VARCHAR(100),
    phone        VARCHAR(30),
    parent_id    BIGINT REFERENCES branch(id),   -- baş ofis -> filial ierarxiyası
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_branch_parent ON branch(parent_id);
CREATE INDEX idx_branch_active ON branch(id) WHERE active = true;

CREATE TRIGGER trg_branch_updated
    BEFORE UPDATE ON branch
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- ---------------------------------------------------------------------
-- ŞÖBƏ (filial daxilində)
-- ---------------------------------------------------------------------
CREATE TABLE department (
    id           BIGSERIAL PRIMARY KEY,
    branch_id    BIGINT NOT NULL REFERENCES branch(id) ON DELETE RESTRICT,
    name         VARCHAR(150) NOT NULL,
    code         VARCHAR(20),
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT uq_department_branch_name UNIQUE (branch_id, name)
);

CREATE INDEX idx_department_branch ON department(branch_id);

CREATE TRIGGER trg_department_updated
    BEFORE UPDATE ON department
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- ---------------------------------------------------------------------
-- VƏZİFƏ / ŞTAT VAHİDİ
-- Diqqət: bu "ştatdakı yer"dir, işçi deyil.
-- Bir vəzifədə headcount say qədər işçi ola bilər.
-- ---------------------------------------------------------------------
CREATE TABLE position (
    id             BIGSERIAL PRIMARY KEY,
    branch_id      BIGINT NOT NULL REFERENCES branch(id) ON DELETE RESTRICT,
    department_id  BIGINT REFERENCES department(id) ON DELETE SET NULL,
    title          VARCHAR(150) NOT NULL,        -- "Baş mühasib"
    code           VARCHAR(30),                   -- ştat cədvəli kodu
    headcount      SMALLINT NOT NULL DEFAULT 1 CHECK (headcount > 0),
    reports_to_id  BIGINT REFERENCES position(id),
    active         BOOLEAN NOT NULL DEFAULT true,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_position_branch     ON position(branch_id);
CREATE INDEX idx_position_department ON position(department_id);
CREATE INDEX idx_position_reports_to ON position(reports_to_id);

CREATE TRIGGER trg_position_updated
    BEFORE UPDATE ON position
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- ---------------------------------------------------------------------
-- İŞÇİ
-- ---------------------------------------------------------------------
CREATE TYPE employee_status AS ENUM (
    'ACTIVE',       -- işləyir
    'ON_LEAVE',     -- məzuniyyət / analıq
    'SUSPENDED',    -- müvəqqəti dayandırılıb
    'TERMINATED'    -- işdən çıxıb
);

CREATE TABLE employee (
    id              BIGSERIAL PRIMARY KEY,
    personnel_no    VARCHAR(30) UNIQUE,           -- tabel nömrəsi
    first_name      VARCHAR(80)  NOT NULL,
    last_name       VARCHAR(80)  NOT NULL,
    patronymic      VARCHAR(80),
    position_id     BIGINT REFERENCES position(id) ON DELETE SET NULL,
    branch_id       BIGINT NOT NULL REFERENCES branch(id) ON DELETE RESTRICT,
    department_id   BIGINT REFERENCES department(id) ON DELETE SET NULL,
    status          employee_status NOT NULL DEFAULT 'ACTIVE',
    hired_at        DATE,
    terminated_at   DATE,
    phone_mobile    VARCHAR(30),
    phone_internal  VARCHAR(20),                  -- daxili nömrə
    ad_username     VARCHAR(100) UNIQUE,          -- DOMAIN\vcafarli -> agent bunu tanıyır
    ad_object_guid  UUID UNIQUE,                  -- Active Directory dəyişməz açar
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT chk_employee_termination
        CHECK (status <> 'TERMINATED' OR terminated_at IS NOT NULL)
);

CREATE INDEX idx_employee_branch     ON employee(branch_id);
CREATE INDEX idx_employee_position   ON employee(position_id);
CREATE INDEX idx_employee_department ON employee(department_id);
CREATE INDEX idx_employee_active     ON employee(branch_id) WHERE status = 'ACTIVE';
CREATE INDEX idx_employee_fullname   ON employee(last_name, first_name);

CREATE TRIGGER trg_employee_updated
    BEFORE UPDATE ON employee
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
