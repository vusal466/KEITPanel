-- =====================================================================
-- V7: equipment_assignment — çatışmayan audit sütunları
-- BaseEntity hər entity-də created_at/updated_at tələb edir,
-- bu sütunlar V2-də səhvən unudulmuşdu.
-- =====================================================================

ALTER TABLE equipment_assignment
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMPTZ NOT NULL DEFAULT now();
