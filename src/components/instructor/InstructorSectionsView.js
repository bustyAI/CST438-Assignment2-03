import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import { SERVER_URL } from '../../Constants';

// instructor views a list of sections they are teaching 
// use the URL /sections?email=dwisneski@csumb.edu&year= &semester=
// the email= will be removed in assignment 7 login security
// The REST api returns a list of SectionDTO objects
// The table of sections contains columns
//   section no, course id, section id, building, room, times and links to assignments and enrollments
// hint:  
// <Link to="/enrollments" state={section}>View Enrollments</Link>
// <Link to="/assignments" state={section}>View Assignments</Link>

const InstructorSectionsView = (props) => {
    const headers = ['Sec No', 'Year', 'Semester', 'Course Id', 'Sec Id', 'Building', 'Room', 'Times',
        'Instructor', 'Instructor Email'];
    const [sections, setSections] = useState([]);

    const location = useLocation();
    const term = location.state;


    const fetchSections = async () => {
        try {
            const response = await fetch(`${SERVER_URL}/sections?email=dwisneski@csumb.edu&year=${parseInt(term.year)}&semester=${term.semester}`);
            if (response.ok) {
                const sections = await response.json();
                setSections(sections);
            } else {
                console.error('Failed to fetch sections');
            }
        } catch (error) {
            console.error('Error fetching sections:', error);
        }
    };

    useEffect(() => {
        fetchSections();
    }, []);

    return (
        <>
            <h3>Instructor Sections</h3>
            <table className="Center">
                <thead>
                    <tr>
                        {headers.map((s, idx) => (<th key={idx}>{s}</th>))}
                    </tr>
                </thead>
                <tbody>
                    {sections.map(section => (
                        <tr key={section.secNo}>
                            <td>{section.secNo}</td>
                            <td>{section.year}</td>
                            <td>{section.semester}</td>
                            <td>{section.courseId}</td>
                            <td>{section.secId}</td>
                            <td>{section.building}</td>
                            <td>{section.room}</td>
                            <td>{section.times}</td>
                            <td>{section.instructorName}</td>
                            <td>{section.instructorEmail}</td>
                            <td>
                                <Link to='/enrollments' id={section.secNo + "enrollments"} state={{ secNo: section.secNo, courseId: section.courseId, secId: section.secId }}>View Enrollments</Link>
                            </td>
                            <td>
                                <Link to='/assignments' id={section.secNo + "assignments"} state={section.secNo}>View Assignments</Link>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </>
    );
};

export default InstructorSectionsView;

