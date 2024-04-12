import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css';
import AssignmentUpdate from './AssignmentUpdate';
import AssignmentAdd from './AssignmentAdd';
import Button from '@mui/material/Button';
import { SERVER_URL } from '../../Constants';
import { Link } from 'react-router-dom';
// instructor views assignments for their section
// use location to get the section value 
// 
// GET assignments using the URL /sections/{secNo}/assignments
// returns a list of AssignmentDTOs
// display a table with columns 
// assignment id, title, dueDate and buttons to grade, edit, delete each assignment

function AssignmentsView(props) {
    const headers = ['Assignment ID', 'Title', 'Due Date', 'Course Id', 'Section Id', 'Section No'];

    const [assignments, setAssignments] = useState([]);
    const [message, setMessage] = useState('');
    const location = useLocation();
    const sectionNo = location.state;

    const fetchAssignments = async (secNo) => {
        try {
            const jwt = sessionStorage.getItem('jwt');
            const response = await fetch(`${SERVER_URL}/sections/${secNo}/assignments`,
            {headers: {
              'Authorization': jwt,
            }});
            if (response.ok) {
                const assignments = await response.json();
                setAssignments(assignments);
            } else {
                const json = await response.json();
                setMessage("Response error: " + json.message);
            }
        } catch (err) {
            setMessage("Network error: " + err);
        }
    }

    useEffect(() => {
        fetchAssignments(sectionNo);
    }, []);

    const addAssignment = async (assignment) => {
        try {
            const jwt = sessionStorage.getItem('jwt');
            const response = await fetch(`${SERVER_URL}/assignments`, {
                method: 'POST',
                headers: {
                    'Authorization': jwt,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(assignment),
            });
            if (response.ok) {
                setMessage("Assignment added successfully");
                fetchAssignments(sectionNo);
            } else {
                const rc = await response.json();
                setMessage(rc.message);
            }
        } catch (err) {
            setMessage("Network error: " + err);
        }
    }

    const updateAssignment = async (assignment) => {
        console.log("view ", assignment);
        try {
            const jwt = sessionStorage.getItem('jwt');
            const response = await fetch(`${SERVER_URL}/assignments`, {
                method: 'PUT',
                headers: {
                    'Authorization': jwt,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(assignment),
            });
            if (response.ok) {
                setMessage("Assignment saved");
                fetchAssignments(sectionNo);
            } else {
                const json = await response.json();
                setMessage("Response error: " + json.message);
            }
        } catch (err) {
            setMessage("Network error: " + err);
        }
    }

    const deleteAssignment = async (assignmentId) => {
        try {
            const jwt = sessionStorage.getItem('jwt');
            const response = await fetch(`${SERVER_URL}/assignments/${assignmentId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': jwt,
                    'Content-Type': 'application/json',
                },
            });
            if (response.ok) {
                setMessage("Assignment deleted");
                fetchAssignments(sectionNo);
            } else {
                const rc = await response.json();
                setMessage("Delete failed " + rc.message);
            }
        } catch (err) {
            setMessage("Network error: " + err);
        }
    }

    const onDelete = (assignmentId) => {
        confirmAlert({
            title: 'Confirm to delete',
            message: 'Do you really want to delete?',
            buttons: [
                {
                    label: 'Yes',
                    onClick: () => deleteAssignment(assignmentId)
                },
                {
                    label: 'No',
                }
            ]
        });
    }

    return (
        <div>
            <h3>Assignments</h3>
            <h4>{message}</h4>
            <table className="Center">
                <thead>
                    <tr>
                        {headers.map((s, idx) => (<th key={idx}>{s}</th>))}
                    </tr>
                </thead>
                <tbody>
                    {assignments.map((a) => (
                        <tr key={a.id}>
                            <td>{a.id}</td>
                            <td>{a.title}</td>
                            <td>{a.dueDate}</td>
                            <td>{a.courseId}</td>
                            <td>{a.secId}</td>
                            <td>{a.secNo}</td>
                            <td><AssignmentUpdate id={a.id + " edit"} assignment={a} state={a} onClose={fetchAssignments} update={updateAssignment} /></td>
                            <td>
                                <Link to='/grades' id={a.id + " grades"} state={a.id}>View Grades</Link>
                            </td>
                            <td><Button id={a.id + " delete"} onClick={() => onDelete(a.id)}>Delete</Button></td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <AssignmentAdd add={addAssignment} />
        </div>
    );
}

export default AssignmentsView;