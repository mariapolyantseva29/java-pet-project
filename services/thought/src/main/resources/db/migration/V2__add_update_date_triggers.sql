CREATE OR REPLACE FUNCTION update_update_date_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER update_situations_update_date
    BEFORE UPDATE ON situations
    FOR EACH ROW
    EXECUTE FUNCTION update_update_date_column();

CREATE TRIGGER update_thoughts_update_date
    BEFORE UPDATE ON thoughts
    FOR EACH ROW
    EXECUTE FUNCTION update_update_date_column();
