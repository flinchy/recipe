INSERT INTO products (id, name, price_in_cents)
VALUES (1, 'Spaghetti 500g', 199.00),
       (2, 'Extra Virgin Olive Oil', 599.00),
       (3, 'Fresh Basil 50g', 249.00),
       (4, 'Parmesan Cheese 100g', 399.00),
       (5, 'Garlic 3 cloves', 129.00),
       (6, 'Cherry Tomatoes 250g', 329.00),
       (7, 'Onion 1pc', 89.00),
       (8, 'Heavy Cream 200ml', 279.00),
       (9, 'Chicken Breast 500g', 749.00),
       (10, 'Lemon 1pc', 99.00);

-- 10 sample recipes
INSERT INTO recipe (id, name, description)
VALUES (1, 'Pesto Pasta', 'Spaghetti tossed with basil pesto, parmesan and olive oil'),
       (2, 'Chicken Alfredo', 'Creamy fettuccine alfredo with grilled chicken breast'),
       (3, 'Tomato Basil Soup', 'Smooth roasted tomato soup finished with fresh basil'),
       (4, 'Lemon Garlic Chicken', 'Pan-seared chicken in a bright lemon–garlic butter sauce'),
       (5, 'Caprese Salad', 'Tomatoes, mozzarella and basil drizzled with olive oil'),
       (6, 'Guacamole', 'Classic avocado dip with lime, onion, cilantro and tomato'),
       (7, 'Beef Tacos', 'Seasoned minced beef served in tortillas with toppings'),
       (8, 'Vegetable Stir-Fry', 'Mixed veggies quickly stir-fried in a soy-ginger sauce'),
       (9, 'Chocolate Chip Cookies', 'Chewy cookies packed with chocolate chips'),
       (10, 'Greek Salad', 'Cucumber, tomato, feta, olives and red onion with oregano');


INSERT INTO recipe_ingredient (product_id, recipe_id)
VALUES
-- 1) Pesto Pasta
(1, 1),   -- Spaghetti 500g
(3, 1),   -- Fresh Basil 50g
(2, 1),   -- Extra Virgin Olive Oil
(4, 1),   -- Parmesan Cheese 100g
(5, 1),   -- Garlic 3 cloves

-- 2) Chicken Alfredo
(9, 2),   -- Chicken Breast 500g
(8, 2),   -- Heavy Cream 200ml
(4, 2),   -- Parmesan Cheese 100g
(5, 2),   -- Garlic 3 cloves
(1, 2),   -- Spaghetti 500g

-- 3) Tomato Basil Soup
(6, 3),   -- Cherry Tomatoes 250g (2 packs)
(7, 3),   -- Onion 1pc
(3, 3),   -- Fresh Basil 50g
(5, 3),   -- Garlic 3 cloves
(2, 3),   -- Extra Virgin Olive Oil

-- 4) Lemon Garlic Chicken
(9, 4),   -- Chicken Breast 500g
(10, 4),  -- Lemon 1pc
(5, 4),   -- Garlic 3 cloves
(2, 4),   -- Extra Virgin Olive Oil

-- 5) Caprese Salad
(6, 5),   -- Cherry Tomatoes 250g
(3, 5),   -- Fresh Basil 50g
(2, 5),   -- Extra Virgin Olive Oil

-- 6) Guacamole
(7, 6),   -- Onion 1pc
(6, 6),   -- Cherry Tomatoes 250g
(10, 6),  -- Lemon 1pc
(5, 6),   -- Garlic 3 cloves

-- 7) Beef Tacos (placeholder ingredients)
(7, 7),   -- Onion
(6, 7),   -- Cherry Tomatoes
(5, 7),   -- Garlic
(2, 7),   -- Olive Oil

-- 8) Vegetable Stir-Fry
(7, 8),   -- Onion
(6, 8),   -- Cherry Tomatoes
(2, 8),   -- Olive Oil
(5, 8),   -- Garlic

-- 9) Chocolate Chip Cookies
(4, 9),   -- Parmesan
(8, 9),   -- Heavy Cream
(2, 9),   -- Olive Oil

-- 10) Greek Salad
(6, 10),  -- Cherry Tomatoes
(3, 10),  -- Fresh Basil
(10, 10), -- Lemon
(2, 10),  -- Olive Oil
(7, 10); -- Onion


INSERT INTO carts (id, total_in_cents)
VALUES (1, 797.00),  -- 2×199 + 1×399 = 398 + 399
       (2, 876.00),  -- 3×249 + 1×129 = 747 + 129
       (3, 2095.00), -- 1×599 + 2×329 + 1×89 + 1×749 = 2095
       (4, 0.00);

INSERT INTO cart_items (cart_id, product_id, quantity)
VALUES
    -- Cart 1
    (1, 1, 2), -- 2× Spaghetti @199
    (1, 4, 1), -- 1× Parmesan @399

    -- Cart 2
    (2, 3, 3), -- 3× Fresh Basil @249
    (2, 5, 1), -- 1× Garlic @129

    -- Cart 3
    (3, 2, 1), -- 1× Olive Oil @599
    (3, 6, 2), -- 2× Cherry Tomatoes @329
    (3, 7, 1), -- 1× Onion @89
    (3, 9, 1); -- 1× Chicken Breast @749