import React, { useState, useEffect } from 'react';
import { Button } from '@mui/material';
import { SERVER_URL } from '../../Constants';

// student can view schedule of sections 
// use the URL /enrollment?studentId=3&year= &semester=
// The REST api returns a list of EnrollmentDTO objects
// studentId=3 will be removed in assignment 7

// to drop a course 
// issue a DELETE with URL /enrollment/{enrollmentId}

const ScheduleView = () => {
  const headers = ['Course ID', 'Section', 'Year', 'Semester'];

  const [enrollments, setEnrollments] = useState([]);
  const [search, setSearch] = useState({ year: "", semester: "" });
  const [message, setMessage] = useState('');

  const resetEnrollments = () => {
    setEnrollments([]);
  };

  const editChange = (event) => {
    setSearch({ ...search, [event.target.name]: event.target.value });
  };

  const fetchEnrollments = async () => {
    try {
      const jwt = sessionStorage.getItem('jwt');
      const response = await fetch(`${SERVER_URL}/enrollments?&year=${search.year}&semester=${search.semester}`,
        {
          headers: {
            'Authorization': jwt,
          }
        });

      if (response.ok) {
        const data = await response.json();
        setEnrollments(data);
        setMessage('');
      } else {
        setMessage('Failed to fetch enrollments');
      }
    } catch (error) {
      setMessage('Network error');
    }
  };

  const dropEnrollment = async (enrollmentId) => {
    console.log(enrollmentId)
    try {
      const jwt = sessionStorage.getItem('jwt');
      const response = await fetch(`${SERVER_URL}/enrollments/${enrollmentId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': jwt,
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        setMessage("Enrollment deleted");
        fetchEnrollments();
      } else {
        const rc = await response.json();
        setMessage("Delete failed: " + rc.message);
      }
    } catch (err) {
      setMessage("Network error: " + err);
    }
  }

  return (
    <>
      <h3>Class Schedule</h3>
      <h4>{message}</h4>
      <h4>Enter year and semester. Example 2024 Spring</h4>
      <table className="Center">
        <tbody>
          <tr>
            <td>Year:</td>
            <td>
              <input type="text" id="syear" name="year" value={search.year} onChange={editChange} />
            </td>
          </tr>
          <tr>
            <td>Semester:</td>
            <td>
              <input type="text" id="ssemester" name="semester" value={search.semester} onChange={editChange} />
            </td>
          </tr>
        </tbody>
      </table>
      <br />
      <button
        type="submit" id="search" onClick={() => { fetchEnrollments(); resetEnrollments(); }}>
        Search for Assignments
      </button>
      <br />
      <table className="Center">
        <thead>
          <tr>
            {headers.map((a, idx) => (
              <th key={idx}>{a}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {enrollments.map((enrollment) => (
            <tr key={enrollment.enrollmentId}>
              <td>{enrollment.courseId}</td>
              <td>{enrollment.sectionNo}</td>
              <td>{enrollment.year}</td>
              <td>{enrollment.semester}</td>
              <td><Button variant="contained" color="secondary" onClick={() => dropEnrollment(enrollment.enrollmentId)}>Drop</Button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
};

export default ScheduleView;