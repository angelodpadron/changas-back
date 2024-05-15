-- Insertar clientes
INSERT INTO customer (id, name, email, photo_url) VALUES
                                                      (10, 'John Doe', 'john.doe@example.com', 'https://cdn.download.ams.birds.cornell.edu/api/v1/asset/71547631/900'),
                                                      (20, 'Jane Smith', 'jane.smith@example.com', 'https://cdn.download.ams.birds.cornell.edu/api/v1/asset/362635561/900');

-- Insertar changas con un proveedor (cliente)
INSERT INTO changa (id, title, description, photo_url, provider_id, available) VALUES
                                                                        (1, 'Plomero', 'Reparo lavabos con fugas y más', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 10, TRUE),
                                                                        (2, 'Pintura de Dormitorio', 'Ofrezco servicios de pintura interior.', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 20, TRUE);

-- Insertar más changas para John Doe (cliente con ID 10)
INSERT INTO changa (id, title, description, photo_url, provider_id, available) VALUES
                                                                        (3, 'Electricista', 'Realizo instalaciones eléctricas y reparaciones.', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 10, TRUE),
                                                                        (4, 'Reparación de Electrodomésticos', 'Soluciono problemas con electrodomésticos.', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 10, TRUE);

-- Insertar más changas para Jane Smith (cliente con ID 20)
INSERT INTO changa (id, title, description, photo_url, provider_id, available) VALUES
                                                                        (5, 'Jardinería', 'Cuido y diseño jardines.', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 20, TRUE),
                                                                        (6, 'Limpieza de Ventanas', 'Limpio ventanas de forma profesional.', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 20, TRUE);

-- Insertar temas para las changas
INSERT INTO changa_topics (changa_id, topics) VALUES
                                                 (1, 'Reparación del Hogar'),
                                                 (1, 'Fontanería'),
                                                 (2, 'Mejoras para el Hogar'),
                                                 (2, 'Pintura'),
                                                 (3, 'Electricidad'),
                                                 (3, 'Reparaciones'),
                                                 (4, 'Electrodomésticos'),
                                                 (4, 'Reparaciones'),
                                                 (5, 'Jardinería'),
                                                 (5, 'Exteriores'),
                                                 (6, 'Limpieza'),
                                                 (6, 'Ventanas');
