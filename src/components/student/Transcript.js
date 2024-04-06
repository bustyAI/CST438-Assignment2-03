import React, {useState, useEffect} from 'react';
import { REGISTRAR_URL } from "../../Constants";
import { GRADEBOOK_URL } from "../../Constants";

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
            // studentId=3 will be removed in assignment 7
            const response = await fetch(`${REGISTRAR_URL}/transcript?studentId=${3}`);
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