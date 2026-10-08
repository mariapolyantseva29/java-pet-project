CREATE OR REPLACE FUNCTION update_update_date_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER update_users_update_date
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_update_date_column();
