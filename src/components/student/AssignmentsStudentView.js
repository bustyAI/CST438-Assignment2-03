import React, { useState } from "react";
import "react-confirm-alert/src/react-confirm-alert.css"; // Import css
import { SERVER_URL } from "../../Constants";

// student views a list of assignments and assignment grades
// use the URL  /assignments?studentId= &year= &semester=
// The REST api returns a list of SectionDTO objects
// Use a value of studentId=3 for now. Until login is implemented in assignment 7.

// display a table with columns  Course Id, Assignment Title, Assignment DueDate, Score

const AssignmentsStudentView = (props) => {
  const headers = ["courseId", "title", "dueDate", "score"];

  const [assignments, setAssignments] = useState([]);
  const [search, setSearch] = useState({ year: "", semester: "" });
  const [message, setMessage] = useState("");

  const editChange = (event) => {
    setSearch({ ...search, [event.target.name]: event.target.value });
  };

  const resetAssignments = () => {
    setAssignments([]);
  };

  const fetchAssignmentsStudent = async () => {
    if (search.year === "" || search.semester === "") {
      setMessage("Enter search parameters");
    } else {
      try {
        // Student ID hardcoded as '3' for now, update later
        const response = await fetch(
          `${SERVER_URL}/assignments?studentId=${3}&year=${
            search.year
          }&semester=${search.semester}`
        );
        if (response.ok) {
          const data = await response.json();
          setAssignments(data);
          setMessage("");
        } else {
          const rc = await response.json();
          setMessage(rc.message);
        }
      } catch (err) {
        setMessage("network error: " + err);
      }
    }
  };

  return (
    <>
      <h3>Assignments</h3>
      <h4>{message}</h4>
      <h4>Enter year and semester. Example 2024 Spring</h4>
      <table className="Center">
        <tbody>
          <tr>
            <td>Year:</td>
            <td>
              <input
                type="text"
                id="syear"
                name="year"
                value={search.year}
                onChange={editChange}
              />
            </td>
          </tr>
          <tr>
            <td>Semester:</td>
            <td>
              <input
                type="text"
                id="ssemester"
                name="semester"
                value={search.semester}
                onChange={editChange}
              />
            </td>
          </tr>
        </tbody>
      </table>
      <br />
      <button
        type="submit"
        id="search"
        onClick={() => {
          fetchAssignmentsStudent();
          resetAssignments();
        }}
      >
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
          {assignments.map((a) => (
            <tr key={a.courseId}>
              <td>{a.courseId}</td>
              <td>{a.title}</td>
              <td>{a.dueDate}</td>
              <td>{a.score}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
};

export default AssignmentsStudentView;
