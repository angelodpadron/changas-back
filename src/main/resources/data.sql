-- Insert customers
INSERT INTO customer (id, name, email, photo_url) VALUES
                                                      (10, 'John Doe', 'john.doe@example.com', 'https://cdn.download.ams.birds.cornell.edu/api/v1/asset/71547631/900'),
                                                      (20, 'Jane Smith', 'jane.smith@example.com', 'https://cdn.download.ams.birds.cornell.edu/api/v1/asset/362635561/900');

-- Insert changas with a provider (customer)
INSERT INTO changa (id, title, description, photo_url, provider_id) VALUES
                                                                        (1, 'Plumber', 'I fix leaky sinks and more', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 10),
                                                                        (2, 'Paint Bedroom', 'I offer interior painting services.', 'https://i0.wp.com/plopdo.com/wp-content/uploads/2021/11/9859.jpg?resize=640%2C400&ssl=1', 20);

-- Assuming `topics` is handled via an element collection, typically implemented as a separate table
-- Assuming the table is named `changa_topics`, and it has a `changa_id` and `topic` columns
INSERT INTO changa_topics (changa_id, topics) VALUES
                                                 (1, 'Home Repair'),
                                                 (1, 'Plumbing'),
                                                 (2, 'Home Improvement'),
                                                 (2, 'Painting');
