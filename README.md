Part of Android Development - ISMIN

Course followed by students of Mines St Etienne, ISMIN - M2 Computer Science.

[![Mines St Etienne](./logo.png)](https://www.mines-stetienne.fr/)

# TP4: Fragments

## 📝 Goal

The goal is to refactor the app by introducing Fragments.

Preparatory work:
- Copy everything from previous TP (except the README)

Then:
- Create a `BookListFragment` fragment
- Update its attributes and factory method (the one called `newInstance`) to handle a `ArrayList<Book>`
- Move the `<RecyclerView>` from the layout of `MainActivity` to the one of `BookListFragment`
- Do the same for all RecyclerView related logic

In `MainActivity`:
- Add a `<ConstraintLayout>` in the layout
- Add some code to create a `BookListFragment` fragment and display it

Then:
- Create a `CreateBookFragment` fragment, if you generate it you can keep only `onCreateView` function and discard everything else
- Move code and layout from `CreateBookActivity` to `CreateBookFragment`
- Create an interface `BookCreator` with a `onBookCreated(book: Book)`
- Use `onAttach` function to save a reference to the activity
- Implement `BookCreator` interface in `MainActivity`

## 🚀 Getting Started

- Start Android Studio
- Select `Open an existing Android Studio project` and pick this directory

That's it! You can code!

## 🛰 Extra

No extra this time!