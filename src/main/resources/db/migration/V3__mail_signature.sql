-- =====================================================================
-- V3: Outlook mail hesabı + imza şablonu
-- =====================================================================

CREATE TYPE mail_status AS ENUM ('PENDING', 'ACTIVE', 'SUSPENDED', 'DISABLED');

CREATE TYPE mail_license AS ENUM (
    'NONE', 'EXCHANGE_ONLY', 'BUSINESS_BASIC', 'BUSINESS_STANDARD',
    'E1', 'E3', 'E5', 'SHARED_MAILBOX'
);


-- ---------------------------------------------------------------------
-- İMZA ŞABLONU
-- Hər işçi üçün ayrıca HTML saxlamırıq — şablon saxlayırıq.
-- Filialın telefonu dəyişəndə bir sətir düzəlirsən, 200 imza yenilənir.
-- ---------------------------------------------------------------------
CREATE TABLE signature_template (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    branch_id     BIGINT REFERENCES branch(id) ON DELETE CASCADE,  -- NULL = bütün şirkət
    -- Placeholder-lar: {{fullName}} {{positionTitle}} {{branchName}}
    --                  {{branchAddress}} {{phoneInternal}} {{phoneMobile}} {{email}}
    html_body     TEXT NOT NULL,
    plain_body    TEXT,                            -- plain-text mail üçün
    language      VARCHAR(5) NOT NULL DEFAULT 'az',
    is_default    BOOLEAN NOT NULL DEFAULT false,
    active        BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Hər filialda yalnız bir default şablon
CREATE UNIQUE INDEX uq_signature_default_branch
    ON signature_template(branch_id, language)
    WHERE is_default = true AND branch_id IS NOT NULL;

-- Şirkət səviyyəsində də yalnız bir default
CREATE UNIQUE INDEX uq_signature_default_global
    ON signature_template(language)
    WHERE is_default = true AND branch_id IS NULL;

CREATE TRIGGER trg_signature_template_updated
    BEFORE UPDATE ON signature_template
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- ---------------------------------------------------------------------
-- MAIL HESABI
-- ---------------------------------------------------------------------
CREATE TABLE mail_account (
    id                    BIGSERIAL PRIMARY KEY,
    employee_id           BIGINT NOT NULL UNIQUE
                              REFERENCES employee(id) ON DELETE CASCADE,
    email                 VARCHAR(150) NOT NULL UNIQUE,
    display_name          VARCHAR(150),            -- Outlook-da görünən ad
    upn                   VARCHAR(150),            -- Azure AD principal name
    aliases               JSONB NOT NULL DEFAULT '[]',   -- ["info@sirket.az"]
    status                mail_status  NOT NULL DEFAULT 'PENDING',
    license               mail_license NOT NULL DEFAULT 'NONE',
    mailbox_quota_gb      SMALLINT,
    mailbox_used_mb       INTEGER,                 -- agent/Graph yeniləyir

    signature_template_id BIGINT REFERENCES signature_template(id) ON DELETE SET NULL,
    signature_override    TEXT,                    -- yalnız istisna hallarda

    -- Agent kompüterdən oxuduğu faktiki imza (müqayisə üçün)
    detected_signature    TEXT,
    signature_synced_at   TIMESTAMPTZ,
    signature_in_sync     BOOLEAN,                 -- şablon == faktiki?

    source                data_source NOT NULL DEFAULT 'MANUAL',
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_mail_account_status ON mail_account(status)
    WHERE status <> 'DISABLED';
CREATE INDEX idx_mail_account_email  ON mail_account(lower(email));
CREATE INDEX idx_mail_out_of_sync    ON mail_account(employee_id)
    WHERE signature_in_sync = false;

CREATE TRIGGER trg_mail_account_updated
    BEFORE UPDATE ON mail_account
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
