import React, { useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { SERVER_URL } from '../../Constants';

// complete the code.  
// instructor adds an assignment to a section
// use mui Dialog with assignment fields Title and DueDate
// issue a POST using URL /assignments to add the assignment

const AssignmentAdd = (props) => {

    const [open, setOpen] = useState(false);
    const [editMessage, setEditMessage] = useState('');
    const [assignment, setAssignment] = useState({ id: '', title: '', dueDate: '', courseId: '', secId: '', secNo: '' });

    const editOpen = () => {
        setAssignment({ id: '', title: '', dueDate: '', courseId: '', secId: '', secNo: '' });
        setEditMessage('');
        setOpen(true);
    };

    const editClose = () => {
        setOpen(false);
        props.onClose(8); // 8 is used for testing functionality
        //setAssignment({ id: '', title: '', dueDate: '', courseId: '', secId: '', secNo: '' });
        //setEditMessage('');
    };

    const editChange = (event) => {
        setAssignment({ ...assignment, [event.target.name]: event.target.value });
    }

    const onSave = async () => {
        if (assignment.id === '' || assignment.title === '' || assignment.dueDate === '' || assignment.courseId === ''
            || assignment.secId === '' || assignment.secNo === '') {
            setEditMessage('Must enter data for id, title, dudDate, courseId, secId, and secNo');
        } else {
            addAssignment(assignment);
        }
    }

    const addAssignment = async (assignment) => {
        try {
            const response = await fetch(`${SERVER_URL}/assignments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(assignment),
            });
            if (response.ok) {
                setEditMessage("Assignment added successfully");
            } else {
                const rc = await response.json();
                setEditMessage(rc.message);
            }
        } catch (err) {
            setEditMessage("Network error: " + err);
        }
    }
    return (
        <div>
            <Button id="addAssignment" onClick={editOpen}>Add Assignment</Button>
            <Dialog open={open}>
                <DialogTitle>Add Assignment</DialogTitle>
                <DialogContent style={{ paddingTop: 20 }}>
                    <h4 id="addMessage">{editMessage}</h4>
                    <TextField style={{ padding: 10 }} fullWidth label="Assignment ID" name="id" value={assignment.id} onChange={editChange} />
                    <TextField style={{ padding: 10 }} fullWidth label="Title" name="title" value={assignment.title.toString()} onChange={editChange} />
                    <TextField style={{ padding: 10 }} fullWidth type="date" label="Due Date" name="dueDate" value={assignment.dueDate} onChange={editChange} />
                    <TextField style={{ padding: 10 }} fullWidth label="CourseId" name="courseId" value={assignment.courseId.toString()} onChange={editChange} />
                    <TextField style={{ padding: 10 }} fullWidth label="Section Id" name="secId" value={assignment.secId} onChange={editChange} />
                    <TextField style={{ padding: 10 }} fullWidth label="Section No" name="secNo" value={assignment.secNo} onChange={editChange} />
                </DialogContent>
                <DialogActions>
                    <Button id="close" color="secondary" onClick={editClose}>Close</Button>
                    <Button id="save" color="primary" onClick={onSave}>Save</Button>
                </DialogActions>
            </Dialog>
        </div>
    )
}

export default AssignmentAdd;