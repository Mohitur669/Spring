import React, { useState, useRef } from 'react';

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
    const [selectedRolls, setSelectedRolls] = useState<number[]>([]);
    const [loading, setLoading] = useState(false);
    const [searchMessage, setSearchMessage] = useState('');
    const [addMessage, setAddMessage] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const messageTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);

    const handleAddFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setAddForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSearchFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setSearchForm(prev => ({ ...prev, [name]: value }));
    };

    const handleAddStudent = async (e: React.FormEvent) => {
        e.preventDefault();
        const { roll, name, marks } = addForm;
        if (!roll || !name || !marks) {
            setAddMessage('Please fill all fields');
            // Hide message after 2 seconds
            if (messageTimeout.current) clearTimeout(messageTimeout.current);
            messageTimeout.current = setTimeout(() => setAddMessage(''), 2000);
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
                setAddMessage(isEditing ? 'Student updated successfully!' : 'Student added');
                setAddForm({ roll: '', name: '', marks: '' });
                setIsEditing(false);
                setSelectedRolls([]);
                // Hide message after 2 seconds
                if (messageTimeout.current) clearTimeout(messageTimeout.current);
                messageTimeout.current = setTimeout(() => setAddMessage(''), 2000);
            } else {
                const errorText = await response.text();
                throw new Error(`Failed: ${errorText}`);
            }
        } catch (error) {
            console.error('Error:', error);
            setAddMessage('Error saving student.');
            if (messageTimeout.current) clearTimeout(messageTimeout.current);
            messageTimeout.current = setTimeout(() => setAddMessage(''), 2000);
        } finally {
            setLoading(false);
        }
    };

    const handleSearchStudent = async (e: React.FormEvent) => {
        e.preventDefault();
        const { roll, name } = searchForm;
        if (!roll && !name) {
            setSearchMessage('Please provide Roll or Name');
            // Clear any previous timeout
            if (messageTimeout.current) clearTimeout(messageTimeout.current);
            messageTimeout.current = setTimeout(() => setSearchMessage(''), 2000);
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
                setSelectedRolls([]);
                setSearchMessage(data.length ? `Found ${data.length} student(s)` : 'No students found.');
            } else {
                setSearchResults([]);
                setSearchMessage('Invalid response format');
            }
        } catch (error) {
            console.error(error);
            setSearchMessage('Error fetching students.');
            setSearchResults([]);
        } finally {
            setLoading(false);
            // Clear any previous timeout
            if (messageTimeout.current) clearTimeout(messageTimeout.current);
            // Hide message after 2 seconds
            messageTimeout.current = setTimeout(() => setSearchMessage(''), 2000);
        }
    };

    const handleEditStudent = (student: Student) => {
        setAddForm({
            roll: student.roll.toString(),
            name: student.name,
            marks: student.marks.toString(),
        });
        setIsEditing(true);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleDeleteSelected = async () => {
        if (!window.confirm("Are you sure you want to delete selected students?")) return;

        setLoading(true);
        try {
            for (const roll of selectedRolls) {
                await fetch(`/students/deleteStudent?roll=${roll}`, { method: 'POST' });
            }
            setAddMessage(`${selectedRolls.length} student(s) deleted successfully.`);
            setSelectedRolls([]);
            handleSearchStudent({ preventDefault: () => {} } as React.FormEvent);
            if (messageTimeout.current) clearTimeout(messageTimeout.current);
            messageTimeout.current = setTimeout(() => setAddMessage(''), 2000);
        } catch (err) {
            console.error(err);
            setAddMessage('Error deleting students.');
            if (messageTimeout.current) clearTimeout(messageTimeout.current);
            messageTimeout.current = setTimeout(() => setAddMessage(''), 2000);
        } finally {
            setLoading(false);
        }
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
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                            disabled={isEditing}
                        />

                        <input
                            type="text"
                            name="name"
                            placeholder="Name"
                            value={addForm.name}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                        />

                        <input
                            type="number"
                            name="marks"
                            placeholder="Marks"
                            value={addForm.marks}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                        />

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition duration-200 disabled:opacity-50"
                        >
                            {loading ? (isEditing ? 'Updating...' : 'Adding...') : isEditing ? 'Update' : 'Add Student'}
                        </button>

                        <label className="inline-block bg-purple-600 hover:bg-purple-700 text-white font-semibold py-2 px-6 rounded-lg cursor-pointer transition duration-200 ml-4 ">
                            Upload Students
                            <input
                                type="file"
                                accept=".xlsx, .xls"
                                onChange={async (e) => {
                                    const file = e.target.files?.[0];
                                    if (!file) return;

                                    const formData = new FormData();
                                    formData.append("file", file);

                                    try {
                                        setLoading(true);
                                        const response = await fetch("/students/uploadExcel", {
                                            method: "POST",
                                            body: formData,
                                        });

                                        const text = await response.text();
                                        setAddMessage(text);
                                        if (response.ok) {
                                            setSearchResults([]);
                                            setAddForm({ roll: '', name: '', marks: '' });
                                            setSearchForm({ roll: '', name: '' });
                                        }
                                    } catch (error) {
                                        console.error("Upload error:", error);
                                        setAddMessage("Error uploading Excel");
                                    } finally {
                                        if (messageTimeout.current) clearTimeout(messageTimeout.current);
                                        messageTimeout.current = setTimeout(() => setAddMessage(''), 2000);
                                        setLoading(false);
                                    }
                                }}
                                className="hidden"
                            />
                        </label>
                    </form>
                    {addMessage && (
                        <div className={`p-4 rounded-lg mt-6 ${addMessage.includes('Error') || addMessage.includes('Please') ?
                            'bg-red-100 text-red-700 border border-red-300' :
                            'bg-green-100 text-green-700 border border-green-300'}`}>
                            {addMessage}
                        </div>
                    )}
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
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                        />

                        <input
                            type="text"
                            name="name"
                            placeholder="Search by Name"
                            value={searchForm.name}
                            onChange={handleSearchFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                        />

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-6 rounded-lg"
                        >
                            {loading ? 'Searching...' : 'Search'}
                        </button>
                    </form>
                    {searchMessage && (
                        <div className={`p-4 rounded-lg mt-6 ${searchMessage.includes('Error') || searchMessage.includes('Please') ?
                            'bg-red-100 text-red-700 border border-red-300' :
                            'bg-green-100 text-green-700 border border-green-300'}`}>
                            {searchMessage}
                        </div>
                    )}
                </div>



                {/* Results */}
                {searchResults.length > 0 && (
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <h3 className="text-xl font-bold text-gray-800 mb-4">Search Results</h3>
                        <div className="overflow-x-auto">
                            <table className="w-full border-collapse">
                                <thead>
                                    <tr className="bg-gray-50">
                                        <th className="border px-4 py-3 text-center">
                                            <input
                                                type="checkbox"
                                                checked={selectedRolls.length === searchResults.length && searchResults.length > 0}
                                                onChange={(e) => {
                                                    if (e.target.checked) {
                                                        setSelectedRolls(searchResults.map(s => s.roll));
                                                    } else {
                                                        setSelectedRolls([]);
                                                    }
                                                }}
                                            />
                                        </th>
                                        <th className="border px-4 py-3 text-center">Roll</th>
                                        <th className="border px-4 py-3 text-center">Name</th>
                                        <th className="border px-4 py-3 text-center">Marks</th>
                                        <th className="border px-4 py-3 text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {searchResults.map((student, index) => (
                                        <tr key={student.id || index} className="hover:bg-gray-50">
                                            <td className="border px-4 py-3 text-center">
                                                <input
                                                    type="checkbox"
                                                    checked={selectedRolls.includes(student.roll)}
                                                    onChange={() => {
                                                        setSelectedRolls((prev) =>
                                                            prev.includes(student.roll)
                                                                ? prev.filter(roll => roll !== student.roll)
                                                                : [...prev, student.roll]
                                                        );
                                                    }}
                                                />
                                            </td>
                                            <td className="border px-4 py-3 text-center">{student.roll}</td>
                                            <td className="border px-4 py-3 text-center">{student.name}</td>
                                            <td className="border px-4 py-3 text-center">{student.marks}</td>
                                            <td className="border px-4 py-3 text-center">
                                                <div className="flex items-center justify-center h-full">
                                                    <button
                                                        className="text-blue-600 hover:underline"
                                                        onClick={() => handleEditStudent(student)}
                                                    >
                                                        Edit
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {/* Delete Selected Button */}
                {selectedRolls.length > 0 && (
                    <button
                        onClick={handleDeleteSelected}
                        className="mb-4 bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded-lg"
                    >
                        Delete Selected ({selectedRolls.length})
                    </button>
                )}
            </div>
        </div>
    );
};

export default App;
