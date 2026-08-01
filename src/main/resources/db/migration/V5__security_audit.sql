-- =====================================================================
-- V5: Sistem istifadəçiləri, rollar, audit
-- =====================================================================

CREATE TABLE app_role (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

INSERT INTO app_role (name, description) VALUES
    ('ROLE_ADMIN',          'Tam giriş, konfiqurasiya'),
    ('ROLE_IT',             'Avadanlıq və mail idarəetməsi'),
    ('ROLE_BRANCH_MANAGER', 'Yalnız öz filialı — oxu + təhkim'),
    ('ROLE_HR',             'İşçi və vəzifə məlumatları'),
    ('ROLE_VIEWER',         'Yalnız oxu');


CREATE TABLE app_user (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,          -- BCrypt
    email         VARCHAR(150),
    employee_id   BIGINT REFERENCES employee(id) ON DELETE SET NULL,
    -- ROLE_BRANCH_MANAGER üçün: hansı filiala icazəsi var
    branch_id     BIGINT REFERENCES branch(id) ON DELETE SET NULL,
    enabled       BOOLEAN NOT NULL DEFAULT true,
    last_login_at TIMESTAMPTZ,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TRIGGER trg_app_user_updated
    BEFORE UPDATE ON app_user
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


CREATE TABLE app_user_role (
    user_id  BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    role_id  BIGINT NOT NULL REFERENCES app_role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);


-- ---------------------------------------------------------------------
-- AUDIT LOG
-- Maddi məsuliyyət olan sistemdir — kim nəyi dəyişdi, bilinməlidir.
-- ---------------------------------------------------------------------
CREATE TABLE audit_log (
    id           BIGSERIAL PRIMARY KEY,
    entity_type  VARCHAR(60)  NOT NULL,           -- "equipment", "employee"
    entity_id    BIGINT       NOT NULL,
    action       VARCHAR(20)  NOT NULL,           -- CREATE / UPDATE / DELETE / ASSIGN
    changed_by   VARCHAR(100) NOT NULL,
    changes      JSONB,                           -- {"status": {"old":"IN_USE","new":"IN_REPAIR"}}
    ip_address   INET,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_entity ON audit_log(entity_type, entity_id, created_at DESC);
CREATE INDEX idx_audit_user   ON audit_log(changed_by, created_at DESC);
CREATE INDEX idx_audit_recent ON audit_log(created_at DESC);


-- ---------------------------------------------------------------------
-- FAYDALI VIEW: hazırda kimdə hansı avadanlıq var
-- ---------------------------------------------------------------------
CREATE VIEW v_current_assignment AS
SELECT
    e.id            AS equipment_id,
    e.inventory_no,
    e.serial_no,
    e.type,
    e.status,
    e.brand,
    e.model,
    e.specs,
    b.name          AS branch_name,
    emp.id          AS employee_id,
    emp.last_name || ' ' || emp.first_name AS employee_name,
    p.title         AS position_title,
    a.assigned_at,
    a.handover_doc_no
FROM equipment e
JOIN branch b               ON b.id = e.branch_id
LEFT JOIN equipment_assignment a
       ON a.equipment_id = e.id AND a.returned_at IS NULL
LEFT JOIN employee emp      ON emp.id = a.employee_id
LEFT JOIN position p        ON p.id = emp.position_id;
