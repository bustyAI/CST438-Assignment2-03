import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'
import 'react-confirm-alert/src/react-confirm-alert.css';
import { SERVER_URL } from '../../Constants';
import { Outlet, Link } from "react-router-dom";

// instructor enters students' grades for an assignment
// fetch the grades using the URL /assignments/{id}/grades
// REST api returns a list of GradeDTO objects
// display the list as a table with columns 'gradeId', 'student name', 'student email', 'score' 
// score column is an input field 
//  <input type="text" name="score" value={g.score} onChange={onChange} />
 

const AssignmentGrade = (props) => {
    const headers = ['gradeId', 'studentName', 'studentEmail', 'assignmentTitle', 'courseId', 'sectionId', 'score'];
    const [grades, setGrades] = useState([]);
    const [message, setMessage] = useState('');

    const location = useLocation();
    const assignment = location.state;
    console.log(assignment);

    const fetchGrades = async (aId) => {
        try {
            const response = await fetch(`${SERVER_URL}/assignments/${aId}/grades`);
            if (response.ok) {
                const grades = await response.json();
                setGrades(grades);
            } else {
                const json = await response.json();
                setMessage("Response error: " + json.message);
            }
        } catch (err) {
            setMessage("Network error: " + err);
        }
    }

    useEffect(() => {
        fetchGrades(assignment); 
    }, []);

    const saveGrade = async (grade) => {
        try {
            const response = await fetch(`${SERVER_URL}/grades`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(grade),
            });
            if (response.ok) {
                setMessage("Grade saved");
                fetchGrades(assignment);
            } else {
                const json = await response.json();
                setMessage("Response error: " + json.message);
            }
        } catch (err) {
            setMessage("Network error: " + err);
        }
    }
    
    const handleGradeChange = async (g, newScore) => {
        // left in function for future error handling
        g.score = newScore;
    }


    return (
        <div>
            <h3>Grades</h3>
            <h4>Searching for AssignmentId: {assignment}</h4>
            <h4 id="gradeMessage">{message}</h4>
            <table className="Center">
                <thead>
                    <tr>
                        {headers.map((s, idx) => (<th key={idx}>{s}</th>))}
                    </tr>
                </thead>
                <tbody>
                    {grades.map((g) => (
                        <tr key={g.gradeId}>
                            <td>{g.gradeId}</td>
                            <td>{g.studentName}</td>
                            <td>{g.studentEmail}</td>
                            <td>{g.assignmentTitle}</td>
                            <td>{g.courseId}</td>
                            <td>{g.sectionId}</td>
                            <td>
                                <input
                                    type="text"
                                    id= {g.gradeId + " score"}
                                    defaultValue={g.score}
                                    onChange={(e) => handleGradeChange(g, e.target.value)}
                                />
                            </td>
                            <td>
                            <button id= {g.gradeId + " save"} onClick={() => saveGrade(grades)}>Save</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AssignmentGrade;