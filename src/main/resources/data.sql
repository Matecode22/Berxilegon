-- Datos iniciales (se cargan despues de que Hibernate crea las tablas:
-- spring.jpa.defer-datasource-initialization=true).
-- Los id no se escriben: las columnas son IDENTITY; el comentario indica cual le toca a cada fila.

-- ---------------------------------------------------------------------
-- PERMISOS
-- ---------------------------------------------------------------------
INSERT INTO permiso (nombre, descripcion) VALUES ('CREATE_USER',           'Crear usuarios');                                     -- 1
INSERT INTO permiso (nombre, descripcion) VALUES ('READ_USER',             'Consultar usuarios');                                 -- 2
INSERT INTO permiso (nombre, descripcion) VALUES ('UPDATE_USER',           'Editar usuarios');                                    -- 3
INSERT INTO permiso (nombre, descripcion) VALUES ('DELETE_USER',           'Eliminar usuarios');                                  -- 4
INSERT INTO permiso (nombre, descripcion) VALUES ('GESTIONAR_ROLES',       'Administrar roles y sus permisos');                   -- 5
INSERT INTO permiso (nombre, descripcion) VALUES ('GESTIONAR_CATEGORIAS',  'Crear, editar y eliminar categorias');                -- 6
INSERT INTO permiso (nombre, descripcion) VALUES ('CREAR_PUBLICACION',     'Publicar articulos para venta, subasta o intercambio'); -- 7
INSERT INTO permiso (nombre, descripcion) VALUES ('EDITAR_PUBLICACION',    'Editar y dar de baja publicaciones propias');         -- 8
INSERT INTO permiso (nombre, descripcion) VALUES ('MODERAR_PUBLICACIONES', 'Dar de baja publicaciones de cualquier usuario');     -- 9
INSERT INTO permiso (nombre, descripcion) VALUES ('COMPRAR',               'Comprar articulos en venta');                         -- 10
INSERT INTO permiso (nombre, descripcion) VALUES ('PUJAR',                 'Participar en subastas');                             -- 11
INSERT INTO permiso (nombre, descripcion) VALUES ('OFERTAR_INTERCAMBIO',   'Proponer intercambios');                              -- 12
INSERT INTO permiso (nombre, descripcion) VALUES ('RESPONDER_OFERTAS',     'Aceptar o rechazar ofertas recibidas');               -- 13
INSERT INTO permiso (nombre, descripcion) VALUES ('COMENTAR',              'Preguntar y responder en publicaciones');             -- 14
INSERT INTO permiso (nombre, descripcion) VALUES ('MODERAR_COMENTARIOS',   'Eliminar comentarios');                               -- 15

-- ---------------------------------------------------------------------
-- ROLES (con prefijo ROLE_, igual que la base, para usar hasRole)
-- ---------------------------------------------------------------------
INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN');     -- 1
INSERT INTO rol (nombre) VALUES ('ROLE_MODERADOR'); -- 2
INSERT INTO rol (nombre) VALUES ('ROLE_USUARIO');   -- 3 (rol que recibe quien se registra)

-- ADMIN: los 15 permisos
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 1);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 2);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 3);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 4);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 5);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 6);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 7);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 8);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 9);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 10);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 11);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 12);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 13);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 14);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (1, 15);

-- MODERADOR: categorias y moderacion
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (2, 6);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (2, 9);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (2, 14);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (2, 15);

-- USUARIO: operar en el marketplace
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 7);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 8);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 10);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 11);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 12);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 13);
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (3, 14);

-- ---------------------------------------------------------------------
-- USUARIOS
-- ---------------------------------------------------------------------
-- Las contrasenas estan hasheadas con BCrypt (nunca en texto plano).
-- Claves para iniciar sesion: carlos -> admin123, maria -> maria123,
-- andres -> andres123, sofia -> sofia123, julian -> julian123, diana -> diana123
INSERT INTO usuario (nombre, correo, contrasena, foto, ciudad, contacto, fecha_registro, estado)
VALUES ('Carlos Ramirez', 'carlos.ramirez@correo.com', '$2a$10$gQfnn5RhttqDZDuln.yqTeKHkRbr5P8CFwrTwX3eb86ip4eCuimo6', 'https://example.com/usuarios/carlos.jpg', 'Cali', '3001112233', TIMESTAMP '2026-01-10 08:00:00', 'ACTIVO');     -- 1
INSERT INTO usuario (nombre, correo, contrasena, foto, ciudad, contacto, fecha_registro, estado)
VALUES ('Maria Lopez', 'maria.lopez@correo.com', '$2a$10$Oq9C8mXvJkwaNXMeZaIZ..pKJluyukuqs.4h6MDiL9yEOntNOWjCG', 'https://example.com/usuarios/maria.jpg', 'Cali', '3002223344', TIMESTAMP '2026-02-03 10:15:00', 'ACTIVO');           -- 2
INSERT INTO usuario (nombre, correo, contrasena, foto, ciudad, contacto, fecha_registro, estado)
VALUES ('Andres Gomez', 'andres.gomez@correo.com', '$2a$10$yQGSY/dV/cAG4ROdQJGZ1e8Y8bFAM4AjbAQLwX8bf7ph4m3C49hS2', NULL, 'Bogota', '3013334455', TIMESTAMP '2026-02-20 14:30:00', 'ACTIVO');                                      -- 3
INSERT INTO usuario (nombre, correo, contrasena, foto, ciudad, contacto, fecha_registro, estado)
VALUES ('Sofia Torres', 'sofia.torres@correo.com', '$2a$10$bpiKQy0jLlNHjV.KZ.wqi.FQtAiOthhnzW3REj6uaSjGualu4JKoi', 'https://example.com/usuarios/sofia.jpg', 'Medellin', '3024445566', TIMESTAMP '2026-03-11 09:45:00', 'ACTIVO');    -- 4
INSERT INTO usuario (nombre, correo, contrasena, foto, ciudad, contacto, fecha_registro, estado)
VALUES ('Julian Vargas', 'julian.vargas@correo.com', '$2a$10$oiCQkolN/2X0NMboxK.7Ee8h1D0vCpuI4NoyCjz5oy8hNKF4bFUfq', NULL, 'Cali', '3035556677', TIMESTAMP '2026-04-02 16:20:00', 'ACTIVO');                                       -- 5
-- inactiva a proposito: no puede publicar, comprar, pujar, ofertar ni comentar
INSERT INTO usuario (nombre, correo, contrasena, foto, ciudad, contacto, fecha_registro, estado)
VALUES ('Diana Ospina', 'diana.ospina@correo.com', '$2a$10$FjiVNe0oKwNrBxpIf/X.DeFw7Oi9yuNEOnpBESv9J3vZeANP3oB1m', NULL, 'Pereira', '3046667788', TIMESTAMP '2026-05-18 11:00:00', 'INACTIVO');                                   -- 6

-- todo usuario con al menos un rol
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 1); -- carlos -> ROLE_ADMIN
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (2, 3); -- maria  -> USUARIO
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (3, 3); -- andres -> USUARIO
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (4, 3); -- sofia  -> USUARIO
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (5, 3); -- julian -> USUARIO
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (6, 3); -- diana  -> USUARIO
-- andres tiene dos roles: es el unico al que se le puede quitar uno
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (3, 2); -- andres -> MODERADOR

-- ---------------------------------------------------------------------
-- CATEGORIAS
-- ---------------------------------------------------------------------
INSERT INTO categoria (nombre, descripcion) VALUES ('Tecnologia',     'Computadores, celulares, consolas y accesorios'); -- 1
INSERT INTO categoria (nombre, descripcion) VALUES ('Libros',         'Libros de texto, novelas y revistas');            -- 2
INSERT INTO categoria (nombre, descripcion) VALUES ('Hogar',          'Muebles, decoracion y electrodomesticos');        -- 3
INSERT INTO categoria (nombre, descripcion) VALUES ('Deportes',       'Bicicletas, implementos y ropa deportiva');       -- 4
INSERT INTO categoria (nombre, descripcion) VALUES ('Musica',         'Instrumentos y equipos de audio');                -- 5
INSERT INTO categoria (nombre, descripcion) VALUES ('Ropa',           'Prendas y accesorios de vestir');                 -- 6
INSERT INTO categoria (nombre, descripcion) VALUES ('Coleccionables', 'Figuras, cartas y articulos de coleccion');      -- 7 (sin publicaciones: se puede eliminar)

-- ---------------------------------------------------------------------
-- PUBLICACIONES
-- ---------------------------------------------------------------------
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (2, 1, 'Portatil Lenovo ThinkPad T14', 'Ryzen 5, 16 GB RAM, 512 GB SSD. Incluye cargador original.', 'BUENO', 'VENTA', 'ACTIVA', DATE '2026-09-01', NULL);          -- 1 venta que acepta intercambio
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (3, 2, 'Calculo de una variable - Stewart', 'Octava edicion, sin subrayar.', 'COMO_NUEVO', 'VENTA', 'ACTIVA', DATE '2026-09-05', NULL);                            -- 2 venta (tuvo una compra cancelada)
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (4, 4, 'Bicicleta de ruta talla M', 'Marco de aluminio, grupo Shimano Sora.', 'BUENO', 'SUBASTA', 'ACTIVA', DATE '2026-10-01', NULL);                              -- 3 subasta abierta
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (2, 5, 'Guitarra acustica Yamaha C40', 'Cuerdas nuevas, incluye estuche blando.', 'BUENO', 'SUBASTA', 'ACTIVA', DATE '2026-10-02', NULL);                          -- 4 subasta vencida con pujas (lista para cerrar)
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (5, 1, 'Consola Nintendo Switch', 'Modelo OLED con dos controles.', 'BUENO', 'INTERCAMBIO', 'ACTIVA', DATE '2026-09-15', NULL);                                   -- 5 intercambio con ofertas
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (3, 3, 'Silla de escritorio ergonomica', 'Soporte lumbar ajustable.', 'BUENO', 'VENTA', 'VENDIDA', DATE '2026-08-20', NULL);                                       -- 6 vendida
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (4, 1, 'Audifonos Sony WH-1000XM4', 'Cancelacion de ruido, caja original.', 'COMO_NUEVO', 'VENTA', 'RESERVADA', DATE '2026-09-10', NULL);                         -- 7 reservada (compra pendiente)
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (3, 6, 'Chaqueta impermeable North Face', 'Talla M, usada una temporada.', 'COMO_NUEVO', 'INTERCAMBIO', 'ACTIVA', DATE '2026-09-12', NULL);                       -- 8 ofrecida en la oferta 1
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (4, 1, 'Tablet Samsung Galaxy Tab A8', '64 GB, con funda.', 'BUENO', 'INTERCAMBIO', 'ACTIVA', DATE '2026-09-18', NULL);                                            -- 9 ofrecida en la oferta 2
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (5, 4, 'Raqueta de tenis Wilson', 'Grip nuevo.', 'ACEPTABLE', 'VENTA', 'DADA_DE_BAJA', DATE '2026-08-25', 'Se vendio por fuera de la plataforma');               -- 10 dada de baja
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (2, 3, 'Lampara de escritorio LED', 'Tres niveles de intensidad.', 'NUEVO', 'SUBASTA', 'ACTIVA', DATE '2026-10-05', NULL);                                        -- 11 subasta vencida sin pujas
INSERT INTO publicacion (usuario_id, categoria_id, titulo, descripcion, estado_conservacion, modo_oferta, estado, fecha_publicacion, motivo_baja)
VALUES (5, 2, 'Cien anos de soledad - edicion conmemorativa', 'Tapa dura, sin uso.', 'NUEVO', 'VENTA', 'ACTIVA', DATE '2026-10-08', NULL);                               -- 12 sin actividad (se puede eliminar)

-- ---------------------------------------------------------------------
-- FOTOS
-- ---------------------------------------------------------------------
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (1,  'https://example.com/fotos/portatil-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (1,  'https://example.com/fotos/portatil-2.jpg', 2);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (3,  'https://example.com/fotos/bicicleta-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (4,  'https://example.com/fotos/guitarra-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (5,  'https://example.com/fotos/switch-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (5,  'https://example.com/fotos/switch-2.jpg', 2);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (7,  'https://example.com/fotos/audifonos-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (8,  'https://example.com/fotos/chaqueta-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (9,  'https://example.com/fotos/tablet-1.jpg', 1);
INSERT INTO foto_publicacion (publicacion_id, url, orden) VALUES (12, 'https://example.com/fotos/libro-1.jpg', 1);

-- ---------------------------------------------------------------------
-- DETALLES DE VENTA (publicaciones en modo VENTA)
-- ---------------------------------------------------------------------
INSERT INTO detalle_venta (publicacion_id, precio, elegible_intercambio, descripcion_intercambio)
VALUES (1, 1800000, TRUE, 'Recibo celular de gama media o tablet, con diferencia a mi favor');
INSERT INTO detalle_venta (publicacion_id, precio, elegible_intercambio, descripcion_intercambio) VALUES (2,  90000,  FALSE, NULL);
INSERT INTO detalle_venta (publicacion_id, precio, elegible_intercambio, descripcion_intercambio) VALUES (6,  250000, FALSE, NULL);
INSERT INTO detalle_venta (publicacion_id, precio, elegible_intercambio, descripcion_intercambio) VALUES (7,  700000, FALSE, NULL);
INSERT INTO detalle_venta (publicacion_id, precio, elegible_intercambio, descripcion_intercambio) VALUES (10, 150000, FALSE, NULL);
INSERT INTO detalle_venta (publicacion_id, precio, elegible_intercambio, descripcion_intercambio) VALUES (12, 60000,  FALSE, NULL);

-- ---------------------------------------------------------------------
-- DETALLES DE SUBASTA (publicaciones en modo SUBASTA)
-- ---------------------------------------------------------------------
-- Las fechas de cierre son relativas al arranque para que los casos de
-- prueba funcionen cualquier dia que se ejecute la aplicacion.
INSERT INTO detalle_subasta (publicacion_id, precio_base, incremento_minimo, fecha_cierre)
VALUES (3, 600000, 50000, LOCALTIMESTAMP + INTERVAL '7' DAY);   -- abierta una semana mas
INSERT INTO detalle_subasta (publicacion_id, precio_base, incremento_minimo, fecha_cierre)
VALUES (4, 300000, 20000, LOCALTIMESTAMP - INTERVAL '1' DAY);   -- vencida: se puede cerrar
INSERT INTO detalle_subasta (publicacion_id, precio_base, incremento_minimo, fecha_cierre)
VALUES (11, 50000, 5000, LOCALTIMESTAMP - INTERVAL '2' HOUR);   -- vencida y sin pujas

-- ---------------------------------------------------------------------
-- PUJAS (cada una supera a la anterior por al menos el incremento minimo)
-- ---------------------------------------------------------------------
INSERT INTO puja (valor, fecha_hora, publicacion_id, usuario_id) VALUES (600000, TIMESTAMP '2026-10-03 10:00:00', 3, 2); -- maria
INSERT INTO puja (valor, fecha_hora, publicacion_id, usuario_id) VALUES (650000, TIMESTAMP '2026-10-04 18:30:00', 3, 5); -- julian (va ganando)
INSERT INTO puja (valor, fecha_hora, publicacion_id, usuario_id) VALUES (300000, TIMESTAMP '2026-10-05 09:00:00', 4, 3); -- andres
INSERT INTO puja (valor, fecha_hora, publicacion_id, usuario_id) VALUES (340000, TIMESTAMP '2026-10-07 20:15:00', 4, 5); -- julian (gana al cerrar)

-- ---------------------------------------------------------------------
-- COMPRAS
-- ---------------------------------------------------------------------
INSERT INTO compra (fecha_compra, estado, motivo_cancelacion, publicacion_id, usuario_id)
VALUES (TIMESTAMP '2026-08-28 15:00:00', 'COMPLETADA', NULL, 6, 5);                                     -- 1 julian compro la silla
INSERT INTO compra (fecha_compra, estado, motivo_cancelacion, publicacion_id, usuario_id)
VALUES (TIMESTAMP '2026-10-15 12:00:00', 'PENDIENTE', NULL, 7, 3);                                      -- 2 andres reservo los audifonos
INSERT INTO compra (fecha_compra, estado, motivo_cancelacion, publicacion_id, usuario_id)
VALUES (TIMESTAMP '2026-09-20 17:45:00', 'CANCELADA', 'El comprador no se presento a la entrega', 2, 4); -- 3 cancelada

-- ---------------------------------------------------------------------
-- OFERTAS DE INTERCAMBIO Y ARTICULOS OFRECIDOS
-- ---------------------------------------------------------------------
INSERT INTO oferta_intercambio (diferencia_dinero, a_favor_de, estado, motivo_rechazo, fecha_oferta, publicacion_id, usuario_id)
VALUES (900000, 'PUBLICADOR', 'PENDIENTE', NULL, TIMESTAMP '2026-10-10 11:20:00', 1, 3);                -- 1 andres ofrece chaqueta + dinero por el portatil
INSERT INTO oferta_intercambio (diferencia_dinero, a_favor_de, estado, motivo_rechazo, fecha_oferta, publicacion_id, usuario_id)
VALUES (0, NULL, 'PENDIENTE', NULL, TIMESTAMP '2026-10-12 08:05:00', 5, 4);                             -- 2 sofia ofrece tablet por la consola
INSERT INTO oferta_intercambio (diferencia_dinero, a_favor_de, estado, motivo_rechazo, fecha_oferta, publicacion_id, usuario_id)
VALUES (0, NULL, 'RECHAZADA', 'Busco algo de menor valor', TIMESTAMP '2026-09-20 19:00:00', 5, 2);      -- 3 rechazada

INSERT INTO articulo_ofertado (oferta_id, publicacion_ofrecida_id) VALUES (1, 8); -- chaqueta
INSERT INTO articulo_ofertado (oferta_id, publicacion_ofrecida_id) VALUES (2, 9); -- tablet
INSERT INTO articulo_ofertado (oferta_id, publicacion_ofrecida_id) VALUES (3, 1); -- portatil

-- ---------------------------------------------------------------------
-- COMENTARIOS (preguntas y respuestas)
-- ---------------------------------------------------------------------
INSERT INTO comentario (texto, fecha, publicacion_id, usuario_id, comentario_padre_id)
VALUES ('Hola, cuanto dura la bateria?', TIMESTAMP '2026-10-03 09:10:00', 1, 5, NULL);                -- 1
INSERT INTO comentario (texto, fecha, publicacion_id, usuario_id, comentario_padre_id)
VALUES ('Alrededor de 6 horas con uso normal.', TIMESTAMP '2026-10-03 12:40:00', 1, 2, 1);            -- 2 respuesta de la vendedora
INSERT INTO comentario (texto, fecha, publicacion_id, usuario_id, comentario_padre_id)
VALUES ('La bicicleta incluye pedales?', TIMESTAMP '2026-10-04 08:00:00', 3, 3, NULL);                -- 3
INSERT INTO comentario (texto, fecha, publicacion_id, usuario_id, comentario_padre_id)
VALUES ('Si, incluye pedales de plataforma.', TIMESTAMP '2026-10-04 09:30:00', 3, 4, 3);              -- 4 respuesta
INSERT INTO comentario (texto, fecha, publicacion_id, usuario_id, comentario_padre_id)
VALUES ('Recibirias una chaqueta impermeable a cambio?', TIMESTAMP '2026-10-09 17:00:00', 5, 3, NULL); -- 5
