-- Drop foreign key constraint if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'FK_student_user')
BEGIN
    ALTER TABLE students DROP CONSTRAINT FK_student_user;
END

-- Drop primary key constraint on id if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'PK_students' AND TABLE_NAME = 'students')
BEGIN
    ALTER TABLE students DROP CONSTRAINT PK_students;
END

-- Drop id column if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'students' AND COLUMN_NAME = 'id')
BEGIN
    ALTER TABLE students DROP COLUMN id;
END

-- Drop created_at column if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'students' AND COLUMN_NAME = 'created_at')
BEGIN
    ALTER TABLE students DROP COLUMN created_at;
END

-- Drop created_by column if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'students' AND COLUMN_NAME = 'created_by')
BEGIN
    ALTER TABLE students DROP COLUMN created_by;
END

-- Drop updated_at column if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'students' AND COLUMN_NAME = 'updated_at')
BEGIN
    ALTER TABLE students DROP COLUMN updated_at;
END

-- Drop updated_by column if exists
IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'students' AND COLUMN_NAME = 'updated_by')
BEGIN
    ALTER TABLE students DROP COLUMN updated_by;
END

-- Add primary key constraint on user_id
ALTER TABLE students ADD CONSTRAINT PK_students PRIMARY KEY (user_id);

-- Add foreign key constraint for user_id
ALTER TABLE students ADD CONSTRAINT FK_student_user FOREIGN KEY (user_id) REFERENCES users(id);
