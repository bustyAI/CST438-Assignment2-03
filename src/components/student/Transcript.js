import React, {useState, useEffect} from 'react';
import { SERVER_URL } from "../../Constants";


// students gets a list of all courses taken and grades
// use the URL /transcript?studentId=
// the REST api returns a list of EnrollmentDTO objects 
// the table should have columns for 
//  Year, Semester, CourseId, SectionId, Title, Credits, Grade

const Transcript = (props) => {
    const headers = ["year", "semester", "courseId", "sectionId", "credits", "grade"];
    const [enrollments, getEnrollments] = useState([]);

    useEffect(() => {
        fetchTranscript();
    }, []);

    const fetchTranscript = async () => {
        try {
            const jwt = sessionStorage.getItem('jwt');
            const response = await fetch(`${SERVER_URL}/transcript`,
            {headers: {
              'Authorization': jwt,
            }});
            if (response.ok) {
                const data = await response.json();
                getEnrollments(data);
            } else {
                throw new Error('Failed to fetch transcript data');
            }
        } catch (error) {
            console.error('Error fetching transcript data:', error);
        }
    };

    return (
      <>
        <h3>Transcript</h3>
        <table className="Center">
          <thead>
            <tr>
              {headers.map((t, idx) => (
                <th key={idx}>{t}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {enrollments.map((t) => (
              <tr key={t.courseId}>
                <td>{t.year}</td>
                <td>{t.semester}</td>
                <td>{t.courseId}</td>
                <td>{t.sectionId}</td>
                <td>{t.credits}</td>
                <td>{t.grade}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </>
    );
}

export default Transcript;