## What you have

- **Page Objects (POM)** in `app/src/androidTest/java/com/example/shop/pages/`
    - `LoginPage`, `ProductsListPage`, `CartPage`, `CheckoutPage`, `ProfilePage`
- **Helpers** in `.../util/`
    - `HelperFunctions` (stable waits/taps/input), `TestConfig` (timeouts)
    - `BaseTest` with an Eyes **`snapshot(tag, waitForText)`** helper
- **Constants** in `.../data/Constants.kt` (demo email/password)
- App has a **🐞 Visual Bugs** toggle (top of Login screen)

Make sure your Applitools API key is set in `BaseTest`.

---

## Your task

Create a test that does this flow using page methods + helpers:

1) **Login (bugs OFF)**
    - Snapshot tag: `Login Page` (wait for: `Welcome back`)
    - Enter email/password (use the constants) and submit

2) **Products**
    - Snapshot tag: `Products Page` (wait for: `Shop`)
    - Add product **id 1** and **id 2**
    - Snapshot tag: `Products page – items added` (wait for: `Shop`)

3) **Cart**
    - Open cart
    - Snapshot tag: `Cart Page` (wait for: `Your Cart`)

4) **Checkout**
    - Go to checkout
    - Snapshot tag: `Checkout Page` (wait for: `Shipping & Payment`)

Open the Applitools dashboard and **Approve** the baselines.

---

## Round 2 — turn bugs ON

Run the **same test** again, but at the very start call:
login.toggleBugs(on = true)

In Applitools dashboard ignore glitch toggle button.

Leave everything else the same.

Expected: Eyes highlights visual diffs (colors, spacing, elevation, etc.).

---


## Rules

- Use the page objects and helpers.
- Use `snapshot(tag, waitForText)` before each visual checkpoint.

---


## Win condition

- One run (bugs OFF): **5 checkpoints** with clean baselines, glitch toggle region ignored.
- One run (bugs ON): the **same 5 checkpoints**, with **mismatches highlighted** in the dashboard.

