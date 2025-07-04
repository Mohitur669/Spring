import React, { useState } from 'react';

interface Student {
    id?: number;
    roll: number;
    name: string;
    marks: number;
}

const App: React.FC = () => {
    const [addForm, setAddForm] = useState({ roll: '', name: '', marks: '' });
    const [searchForm, setSearchForm] = useState({ roll: '', name: '' });
    const [searchResults, setSearchResults] = useState<Student[]>([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [isEditing, setIsEditing] = useState(false);

    // Handle form changes
    const handleAddFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setAddForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSearchFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setSearchForm(prev => ({ ...prev, [name]: value }));
    };

    // Add or Update student
    const handleAddStudent = async (e: React.FormEvent) => {
        e.preventDefault();

        const { roll, name, marks } = addForm;
        if (!roll || !name || !marks) {
            setMessage('Please fill all fields');
            return;
        }

        try {
            setLoading(true);
            const studentData = {
                roll: parseInt(roll),
                name,
                marks: parseInt(marks),
            };

            const response = await fetch('/students/createStudentFromAPI', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(studentData),
            });

            if (response.ok) {
                setMessage(isEditing ? 'Student updated successfully!' : 'Student added successfully!');
                setAddForm({ roll: '', name: '', marks: '' });
                setIsEditing(false);
                handleSearchStudent({ preventDefault: () => {} } as React.FormEvent);
            } else {
                const errorText = await response.text();
                throw new Error(`Failed: ${errorText}`);
            }
        } catch (error) {
            console.error('Error:', error);
            setMessage('Error saving student.');
        } finally {
            setLoading(false);
        }
    };

    // Search student
    const handleSearchStudent = async (e: React.FormEvent) => {
        e.preventDefault();

        const { roll, name } = searchForm;
        if (!roll && !name) {
            setMessage('Please provide Roll or Name');
            return;
        }

        try {
            setLoading(true);
            const params = new URLSearchParams();
            if (roll) params.append('roll', roll);
            if (name) params.append('name', name);

            const response = await fetch(`/students/findStudents?${params.toString()}`);

            if (!response.ok) throw new Error('Search failed');

            const contentType = response.headers.get('Content-Type');
            if (contentType?.includes('application/json')) {
                const data: Student[] = await response.json();
                setSearchResults(data);
                setMessage(data.length ? `Found ${data.length} student(s)` : 'No students found.');
            } else {
                setSearchResults([]);
                setMessage('Invalid response format');
            }
        } catch (error) {
            console.error(error);
            setMessage('Error fetching students.');
            setSearchResults([]);
        } finally {
            setLoading(false);
        }
    };

    // Populate form for update
    const handleEditStudent = (student: Student) => {
        setAddForm({
            roll: student.roll.toString(),
            name: student.name,
            marks: student.marks.toString(),
        });
        setIsEditing(true);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <div className="bg-blue-600 text-white p-4">
                <h1 className="text-3xl font-semibold text-center">Student Management UI</h1>
            </div>

            <div className="container mx-auto p-6 max-w-4xl">
                {/* Add / Update Section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">
                        {isEditing ? 'Update Student' : 'Add Student'}
                    </h2>

                    <form className="space-y-4" onSubmit={handleAddStudent}>
                        <input
                            type="text"
                            name="roll"
                            placeholder="Roll"
                            value={addForm.roll}
                            onChange={(e) => {
                                if (isEditing) {
                                    alert('Cannot update Roll No');
                                    return;
                                }
                                handleAddFormChange(e);
                            }}
                            onFocus={() => {
                                if (isEditing) alert('Cannot update Roll No');
                            }}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                            disabled={isEditing}
                        />

                        <input
                            type="text"
                            name="name"
                            placeholder="Name"
                            value={addForm.name}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />

                        <input
                            type="number"
                            name="marks"
                            placeholder="Marks"
                            value={addForm.marks}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition duration-200 disabled:opacity-50"
                        >
                            {loading ? (isEditing ? 'Updating...' : 'Adding...') : isEditing ? 'Update' : 'Add Student'}
                        </button>
                    </form>
                </div>

                {/* Search Section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">Search Student</h2>

                    <form className="space-y-4" onSubmit={handleSearchStudent}>
                        <input
                            type="text"
                            name="roll"
                            placeholder="Search by Roll"
                            value={searchForm.roll}
                            onChange={handleSearchFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                        />

                        <input
                            type="text"
                            name="name"
                            placeholder="Search by Name"
                            value={searchForm.name}
                            onChange={handleSearchFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                        />

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-6 rounded-lg transition duration-200 disabled:opacity-50"
                        >
                            {loading ? 'Searching...' : 'Search'}
                        </button>
                    </form>
                </div>

                {/* Message */}
                {message && (
                    <div className={`p-4 rounded-lg mb-6 ${message.includes('Error') || message.includes('Please') ?
                        'bg-red-100 text-red-700 border border-red-300' :
                        'bg-green-100 text-green-700 border border-green-300'}`}>
                        {message}
                    </div>
                )}

                {/* Results */}
                {searchResults.length > 0 && (
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <h3 className="text-xl font-bold text-gray-800 mb-4">Search Results</h3>
                        <div className="overflow-x-auto">
                            <table className="w-full border-collapse">
                                <thead>
                                    <tr className="bg-gray-50">
                                        <th className="border px-4 py-3">Roll</th>
                                        <th className="border px-4 py-3">Name</th>
                                        <th className="border px-4 py-3">Marks</th>
                                        <th className="border px-4 py-3">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {searchResults.map((student, index) => (
                                        <tr key={student.id || index} className="hover:bg-gray-50">
                                            <td className="border px-4 py-3">{student.roll}</td>
                                            <td className="border px-4 py-3">{student.name}</td>
                                            <td className="border px-4 py-3">{student.marks}</td>
                                            <td className="border px-4 py-3">
                                                <button
                                                    className="text-blue-600 hover:underline"
                                                    onClick={() => handleEditStudent(student)}
                                                >
                                                    Edit
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default App;
