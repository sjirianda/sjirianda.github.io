from flask import Flask, request, jsonify
from AnimalShelter import AnimalShelter

app = Flask(__name__)
db = AnimalShelter()

# GET all animals
@app.route('/animals', methods=['GET'])
def get_animals():
    query = request.args.to_dict() or {}
    results = db.read(query)
    return jsonify(results), 200

# POST a new animal
@app.route('/animals', methods=['POST'])
def add_animal():
    data = request.get_json()
    if data:
        success = db.create(data)
        return jsonify({'success': success}), 201 if success else 400
    return jsonify({'error': 'Missing JSON data'}), 400

# PUT to update existing animal(s)
@app.route('/animals', methods=['PUT'])
def update_animal():
    body = request.get_json()
    if body and 'query' in body and 'new_values' in body:
        modified_count = db.update(body['query'], body['new_values'])
        return jsonify({'modified_count': modified_count}), 200
    return jsonify({'error': 'Expected JSON with query and new_values'}), 400

# DELETE animal(s)
@app.route('/animals', methods=['DELETE'])
def delete_animal():
    query = request.get_json()
    if query:
        deleted_count = db.delete(query)
        return jsonify({'deleted_count': deleted_count}), 200
    return jsonify({'error': 'Expected JSON with query'}), 400

# POST to import animals from CSV
@app.route('/import', methods=['POST'])
def import_animals():
    count = db.import_csv('aac_shelter_outcomes.csv')
    return jsonify({'imported': count}), 200 if count > 0 else 400

if __name__ == '__main__':
    app.run(debug=True)
