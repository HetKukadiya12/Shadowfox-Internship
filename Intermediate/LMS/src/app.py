"""
Command-line Library Management System v2 (with Recommendations).
Generated on 2025-06-26.
"""
import getpass
import sys
from db import init_db, get_conn
import requests, json

USER = None

def register():
    username = input("Username: ").strip()
    password = getpass.getpass()
    with get_conn() as conn:
        try:
            conn.execute("INSERT INTO users(username, password) VALUES(?,?)", (username, password))
            conn.commit()
            print("User created.")
        except Exception as e:
            print("Error:", e)

def login():
    global USER
    username = input("Username: ").strip()
    password = getpass.getpass()
    with get_conn() as conn:
        cur = conn.execute("SELECT id, password FROM users WHERE username=?", (username,))
        row = cur.fetchone()
        if row and row[1] == password:
            USER = (row[0], username)
            print("Logged in as", username)
        else:
            print("Invalid credentials")

def add_book():
    title = input("Title: ")
    author = input("Author: ")
    isbn = input("ISBN: ")
    with get_conn() as conn:
        conn.execute("INSERT INTO books(title, author, isbn) VALUES(?,?,?)", (title, author, isbn))
        conn.commit()
        print("Book added.")

def list_books():
    with get_conn() as conn:
        cur = conn.execute("SELECT id,title,author,isbn,available FROM books")
        for row in cur.fetchall():
            status = "Available" if row[4] else "Borrowed"
            print(f"#{row[0]} {row[1]} by {row[2]} [{row[3]}] - {status}")

def borrow():
    if USER is None:
        print("Login first.")
        return
    book_id = input("Book ID: ").strip()
    with get_conn() as conn:
        cur = conn.execute("SELECT available FROM books WHERE id=?", (book_id,))
        row = cur.fetchone()
        if not row:
            print("Book not found.")
            return
        if not row[0]:
            print("Book already borrowed.")
            return
        conn.execute("UPDATE books SET available=0 WHERE id=?", (book_id,))
        conn.execute("INSERT INTO borrows(user_id, book_id) VALUES(?,?)", (USER[0], book_id))
        conn.commit()
        print("Borrowed.")

def return_book():
    if USER is None:
        print("Login first.")
        return
    book_id = input("Book ID: ").strip()
    with get_conn() as conn:
        conn.execute("UPDATE books SET available=1 WHERE id=?", (book_id,))
        conn.execute("UPDATE borrows SET return_date=CURRENT_TIMESTAMP WHERE book_id=? AND user_id=? AND return_date IS NULL",
                     (book_id, USER[0]))
        conn.commit()
        print("Returned.")

def recommend():
    """Recommend books similar to given ISBN using Open Library."""
    isbn = input("ISBN of a book you like: ").strip()
    if not isbn:
        print("Provide a valid ISBN.")
        return
    url = f"https://openlibrary.org/api/books?bibkeys=ISBN:{isbn}&jscmd=data&format=json"
    try:
        data = requests.get(url, timeout=5).json()
    except Exception as e:
        print("API error:", e)
        return
    key = f"ISBN:{isbn}"
    book = data.get(key)
    if not book:
        print("Book not found in API.")
        return
    subjects = [s['name'] for s in book.get('subjects', [])][:3]
    if not subjects:
        print("No subjects found for recommendations.")
        return
    print("Subjects:", ", ".join(subjects))
    rec_url = f"https://openlibrary.org/subjects/{subjects[0].replace(' ', '_').lower()}.json?limit=5"
    try:
        rec_data = requests.get(rec_url, timeout=5).json()
        print("\nRecommendations:")
        for work in rec_data.get('works', []):
            title = work.get('title')
            authors = ', '.join(a['name'] for a in work.get('authors', []))
            print(f"- {title} by {authors}")
    except Exception as e:
        print("Recommendation error:", e)


def help_menu():
    print("Commands: register, login, add, list, borrow, return, help, quit, recommend")

def main():
    init_db()
    help_menu()
    while True:
        cmd = input("> ").strip().lower()
        if cmd == "register":
            register()
        elif cmd == "login":
            login()
        elif cmd == "add":
            add_book()
        elif cmd == "list":
            list_books()
        elif cmd == "borrow":
            borrow()
        elif cmd == "return":
            return_book()
        elif cmd == 'recommend':
            recommend()
        elif cmd in ("help", "?"):
            help_menu()
        elif cmd in ("quit", "exit"):
            sys.exit()
        else:
            print("Unknown command")

if __name__ == "__main__":
    main()
