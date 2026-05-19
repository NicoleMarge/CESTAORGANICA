CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    nombre TEXT NOT NULL,
    correo TEXT UNIQUE NOT NULL,
    usuario TEXT UNIQUE NOT NULL,
    telefono TEXT,
    direccion TEXT,
    password TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

ALTER TABLE usuarios ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Permitir insertar usuarios"
ON usuarios
FOR INSERT
TO anon
WITH CHECK (true);

CREATE POLICY "Permitir leer usuarios"
ON usuarios
FOR SELECT
TO anon
USING (true);