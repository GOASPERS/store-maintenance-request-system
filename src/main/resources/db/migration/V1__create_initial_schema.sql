CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE stores (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(150) NOT NULL,
    contact_person VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE equipment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(150) NOT NULL,
    serial_number VARCHAR(150) NOT NULL UNIQUE,
    store_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_equipment_store
        FOREIGN KEY (store_id) REFERENCES stores (id)
);

CREATE TABLE maintenance_requests (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    store_id BIGINT NOT NULL,
    equipment_id BIGINT,
    created_by_user_id BIGINT NOT NULL,
    assigned_engineer_id BIGINT,
    due_date DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_maintenance_requests_store
        FOREIGN KEY (store_id) REFERENCES stores (id),
    CONSTRAINT fk_maintenance_requests_equipment
        FOREIGN KEY (equipment_id) REFERENCES equipment (id),
    CONSTRAINT fk_maintenance_requests_created_by_user
        FOREIGN KEY (created_by_user_id) REFERENCES users (id),
    CONSTRAINT fk_maintenance_requests_assigned_engineer
        FOREIGN KEY (assigned_engineer_id) REFERENCES users (id)
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    maintenance_request_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_comments_maintenance_request
        FOREIGN KEY (maintenance_request_id) REFERENCES maintenance_requests (id),
    CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE status_history (
    id BIGSERIAL PRIMARY KEY,
    maintenance_request_id BIGINT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    changed_by_user_id BIGINT NOT NULL,
    changed_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_status_history_maintenance_request
        FOREIGN KEY (maintenance_request_id) REFERENCES maintenance_requests (id),
    CONSTRAINT fk_status_history_changed_by_user
        FOREIGN KEY (changed_by_user_id) REFERENCES users (id)
);
