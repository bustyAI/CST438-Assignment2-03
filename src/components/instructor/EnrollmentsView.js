import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { SERVER_URL } from '../../Constants';
import { Button } from '@mui/material';

// instructor view list of students enrolled in a section 
// use location to get section no passed from InstructorSectionsView
// fetch the enrollments using URL /sections/{secNo}/enrollments
// display table with columns
//   'enrollment id', 'student id', 'name', 'email', 'grade'
//  grade column is an input field
//  hint:  <input type="text" name="grade" value={e.grade} onChange={onGradeChange} />

const EnrollmentsView = (props) => {

    const headers = ['Enrollment Id', "Student Id", "Name", "Email", "Grade"];
    const location = useLocation();

    const { secNo, courseId, secId } = location.state || {};
    const [enrollments, setEnrollments] = useState([]);
    const [message, setMessage] = useState('');

    const fetchEnrollments = async (secNo) => {
        try {
            const response = await fetch(`${SERVER_URL}/sections/${secNo}/enrollments`);
            if (response.ok) {
                const students = await response.json();
                console.log(students);
                setEnrollments(students);
            } else {
                const json = await response.json();
                setMessage("Response error: " + json.message)
            }
        } catch (err) {
            setMessage("Network Error: " + err);
        }
    }

    const saveGrade = async (enrollment) => {
        try {
            const response = await fetch(`${SERVER_URL}/enrollments`,
                {
                    method: "PUT",
                    headers: {
                        'Content-Type': "application/json"
                    },
                    body: JSON.stringify([enrollment]),
                });
            if (response.ok) {
                fetchEnrollments(secNo);
                setMessage("Successfully updated grades")
            } else {
                const json = response.json();
                setMessage("Response error: " + json.message);
            }
        } catch (err) {
            setMessage("Network Error: " + message)
        }
    }

    const onGradeChange = (index, newGrade) => {
        const updatedEnrollments = enrollments.map((enrollment, idx) =>
            idx === index ? { ...enrollment, grade: newGrade } : enrollment
        );
        setEnrollments(updatedEnrollments);
        console.log(newGrade);
    }

    useEffect(() => {
        fetchEnrollments(secNo)
    }, [])

    return (
        <div>
            <h4 id='enrollmentGradeMessage'>{message}</h4>
            <table className='Center'>

                <thead>
                    <tr>
                        {headers.map((h, idx) => (<th key={idx}>{h}</th>))}
                    </tr>
                </thead>
                <tbody>
                    {enrollments.map((enrollment, index) => (
                        <tr key={index}>
                            <td>{enrollment.enrollmentId}</td>
                            <td>{enrollment.studentId}</td>
                            <td>{enrollment.name}</td>
                            <td>{enrollment.email}</td>
                            <td><input
                                id={enrollment.enrollmentId + "enrollmentGrade"}
                                type='text'
                                name='grade'
                                value={enrollment.grade || ""}
                                onChange={(e) => onGradeChange(index, e.target.value)}></input>
                            </td>
                            <td>
                                <Button id={enrollment.enrollmentId + 'saveEnrollmentGrade'} onClick={() => saveGrade(enrollment)}>Save Grade</Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default EnrollmentsView;
