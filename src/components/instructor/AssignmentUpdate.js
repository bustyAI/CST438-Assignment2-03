import React, { useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';

//  instructor updates assignment title, dueDate 
//  use an mui Dialog
//  issue PUT to URL  /assignments with updated assignment

const AssignmentUpdate = (props) => {

  const [open, setOpen] = useState(false);
  const [editMessage, setEditMessage] = useState("")
  const [assignment, setAssignment] = useState({ id: '', title: '', dueDate: '', courseId: '', secId: '', secNo: '' })

  const editOpen = (event) => {
    setOpen(true);
    setEditMessage('');
    setAssignment(props.assignment);
  }

  const editClose = () => {
    setOpen(false);
    setAssignment({ id: '', title: '', dueDate: '', courseId: '', secId: '', secNo: '' })
  }

  const editChange = (event) => {
    setAssignment({ ...assignment, [event.target.name]: event.target.value })
  }

  const onSave = () => {
    if (assignment.id === '' || ! /^\d+$/.test(assignment.id)) {
      setEditMessage("AssignmentId can not be blank and must be integer")
    } else if (assignment.title === '') {
      setEditMessage('Title can not be blank');
    } else if (assignment.courseId === '') {
      setEditMessage('CourseId can not be blank')
    } else if (! /^\d+$/.test(assignment.secId)) {
      setEditMessage("SecId must be an integer")
    } else if (! /^\d+$/.test(assignment.secNo)) {
      setEditMessage("SecNo must be an integer")
    } else {
      props.save(assignment)
      editClose();
    }
  }

  return (
    <div>
      <Button onClick={editOpen}>Edit</Button>
      <Dialog open={open}>
        <DialogTitle>Edit Assignment</DialogTitle>
        <DialogContent style={{ paddingTop: 20 }}>
          <h4>{editMessage}</h4>
          <TextField style={{ padding: 10 }} fullWidth label='title' name='title' value={assignment.title} onChange={editChange} />
          <TextField style={{ padding: 10 }} fullWidth type='date' name='dueDate' value={assignment.dueDate} onChange={editChange} />
        </DialogContent>
        <DialogActions>
          <Button color='secondary' onClick={editClose}>Close</Button>
          <Button color='primary' onClick={onSave}>Save</Button>
        </DialogActions>
      </Dialog>
    </div>
  )
}

export default AssignmentUpdate;
