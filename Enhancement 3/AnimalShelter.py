from pymongo import MongoClient
from pymongo.errors import PyMongoError
import csv  # For CSV import

class AnimalShelter(object):
    """ CRUD operations for Animal collection in MongoDB """

    def __init__(self):
        # Local MongoDB Connection — no username, password, or custom port
        HOST = 'localhost'
        PORT = 27017
        DB = 'AAC'
        COL = 'animals'

        # Initialize Connection
        try:
            self.client = MongoClient(f'mongodb://{HOST}:{PORT}')
            self.database = self.client[DB]
            self.collection = self.database[COL]
            print("Connected to local MongoDB.")
        except PyMongoError as e:
            print(f"Error connecting to MongoDB: {e}")
            raise

    # Create method (C in CRUD)
    def create(self, data):
        if data is not None and isinstance(data, dict):
            try:
                result = self.collection.insert_one(data)
                return True if result.inserted_id else False
            except PyMongoError as e:
                print(f"Insert failed: {e}")
                return False
        else:
            raise ValueError("Nothing to save, because data parameter is empty or invalid")

    # Read method (R in CRUD)
    def read(self, query):
        if query is not None and isinstance(query, dict):
            try:
                cursor = self.collection.find(query)
                return list(cursor)
            except PyMongoError as e:
                print(f"Read failed: {e}")
                return []
        else:
            raise ValueError("Query parameter must be a dictionary")

    # Update method (U in CRUD)
    def update(self, query, new_values):
        if query is not None and new_values is not None:
            if isinstance(query, dict) and isinstance(new_values, dict):
                try:
                    result = self.collection.update_many(query, {"$set": new_values})
                    return result.modified_count
                except PyMongoError as e:
                    print(f"Update failed: {e}")
                    return 0
            else:
                raise ValueError("Query and new_values must be dictionaries")
        else:
            raise ValueError("Query and new_values parameters must not be None")

    # Delete method (D in CRUD)
    def delete(self, query):
        if query is not None and isinstance(query, dict):
            try:
                result = self.collection.delete_many(query)
                return result.deleted_count
            except PyMongoError as e:
                print(f"Delete failed: {e}")
                return 0
        else:
            raise ValueError("Query parameter must be a dictionary")

    # Import method (CSV to MongoDB)
    def import_csv(self, csv_path):
        try:
            with open(csv_path, mode='r', encoding='utf-8') as file:
                reader = csv.DictReader(file)
                records = list(reader)
                if records:
                    result = self.collection.insert_many(records)
                    print(f"Inserted {len(result.inserted_ids)} documents.")
                    return len(result.inserted_ids)
                return 0
        except Exception as e:
            print(f"CSV import failed: {e}")
            return 0
