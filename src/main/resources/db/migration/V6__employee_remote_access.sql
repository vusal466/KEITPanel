-- =====================================================================
-- V6: Employee — AD sahələri çıxarılır, uzaqdan dəstək sahələri əlavə olunur
-- =====================================================================

ALTER TABLE employee DROP COLUMN IF EXISTS ad_username;
ALTER TABLE employee DROP COLUMN IF EXISTS ad_object_guid;

ALTER TABLE employee ADD COLUMN anydesk_id  VARCHAR(20);
ALTER TABLE employee ADD COLUMN pc_username VARCHAR(50);

-- Eyni AnyDesk ID iki işçidə ola bilməz (NULL-lar istisna)
CREATE UNIQUE INDEX uq_employee_anydesk ON employee(anydesk_id)
    WHERE anydesk_id IS NOT NULL;
