"""
Database utilities for Library Management System v2.
Auto-generated on 2025-06-26.
"""
import sqlite3
from contextlib import contextmanager
import pathlib

DB_FILE = pathlib.Path(__file__).parent / "library.db"

def init_db():
    with sqlite3.connect(DB_FILE) as conn:
        c = conn.cursor()
        c.execute("""CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE,
            password TEXT
        );""")
        c.execute("""CREATE TABLE IF NOT EXISTS books (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            author TEXT,
            isbn TEXT UNIQUE,
            available INTEGER DEFAULT 1
        );""")
        c.execute("""CREATE TABLE IF NOT EXISTS borrows (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER,
            book_id INTEGER,
            borrow_date TEXT DEFAULT CURRENT_TIMESTAMP,
            return_date TEXT
        );""")
        conn.commit()

@contextmanager
def get_conn():
    with sqlite3.connect(DB_FILE) as conn:
        yield conn
