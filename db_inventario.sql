insert into 
  branch (
    direccion, 
    name
  )
values
  ('Av. Corrientes 1234, CABA', 'Sucursal Centro'),
  ('Av. Santa Fe 5678, Belgrano', 'Sucursal Belgrano'),
  ('Calle Florida 890, Microcentro', 'Sucursal Florida'),
  ('Av. Cabildo 3456, Nuñez', 'Sucursal Nuñez'),
  ('Av. Rivadavia 7890, Flores', 'Sucursal Flores'),
  ('Calle San Martín 234, Mendoza', 'Sucursal Mendoza Centro'),
  ('Av. Colón 4567, Córdoba', 'Sucursal Córdoba'),
  ('Calle 9 de Julio 123, Rosario', 'Sucursal Rosario'),
  ('Av. Libertador 5678, San Isidro', 'Sucursal San Isidro'),
  ('Av. del Libertador 9012, Palermo', 'Sucursal Palermo');;
  
insert into 
    warehouse (
      name, 
      branch_id
    )
  values
    ('Depósito Central', 1),
    ('Depósito Anexo', 1),
    ('Depósito Norte', 2),
    ('Depósito Sur', 2),
    ('Depósito Microcentro', 3),
    ('Depósito Zona Norte', 4),
    ('Depósito Oeste', 5),
    ('Depósito Mendoza Centro', 6),
    ('Depósito Córdoba', 7),
    ('Depósito Rosario', 8);
    

insert into 
  product (
    name, 
    sku, 
    stock, 
    warehouse_id
  )
values
  ('Laptop HP Pavilion', 'HP-PAV-15-001', 25, 1),
  ('Mouse Logitech MX Master', 'LOG-MX3-002', 150, 1),
  ('Teclado Mecánico RGB', 'TEC-MEC-003', 75, 2),
  ('Monitor Samsung 24"', 'SAM-LED-24-004', 40, 2),
  ('Disco SSD 1TB', 'SSD-1TB-005', 200, 3),
  ('Memoria RAM 16GB', 'RAM-16G-006', 120, 3),
  ('Impresora HP Laser', 'HP-LSR-007', 15, 4),
  ('Auriculares Sony WH-1000', 'SON-WH-008', 60, 5),
  ('Webcam Logitech C920', 'LOG-C920-009', 45, 6),
  ('Tablet Samsung Galaxy', 'SAM-TAB-010', 30, 7),
  ('Router TP-Link', 'TPL-RT-011', 85, 8),
  ('Pendrive 64GB', 'PEN-64-012', 300, 9),
  ('Cargador Universal', 'CAR-UNI-013', 110, 10);