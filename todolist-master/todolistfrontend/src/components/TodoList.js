import React, { useState, useEffect } from "react";
import API from "../api";
import { useNavigate } from "react-router-dom";
import "./TodoList.css";

const TodoList = () => {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [editingTask, setEditingTask] = useState(null);  // Store the task being edited
  const [editedDescription, setEditedDescription] = useState("");  // Store edited description
  const navigate = useNavigate();

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const response = await API.get("/task");
      console.log("response data : " ,response);
      if (Array.isArray(response.data)) {
        setTasks(response.data);
      } else {
        console.log("Unexpected data structure:", response.data);
      }
    } catch (error) {
      handleError(error);
    }
  };

  const handleError = (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        alert("Session expired. Please log in again.");
        localStorage.removeItem("token");
        navigate("/login");
      } else {
        alert("Error fetching tasks: " + error.response.data.message);
      }
    } else {
      alert("Network error: " + error.message);
    }
  };

  const addTask = async () => {
    if (newTask.trim()) {
      try {
        const response = await API.post("/task", {
          description: newTask,
          completed: false,
        });
        setTasks([...tasks, response.data]);
        setNewTask("");
        fetchTasks();
      } catch (error) {
        alert("Error adding task: " + error.message);
      }
    }
  };

  const deleteTask = async (id) => {
    try {
      await API.delete(`/task/${id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setTasks(tasks.filter((task) => task.id !== id));
      fetchTasks();
    } catch (error) {
      alert("Error deleting task: " + error.message);
    }
  };

  const toggleTaskCompletion = async (task) => {
    try {
      const updatedTask = { ...task, completed: !task.completed };
      const response = await API.put(`/task/${task.id}`, updatedTask);
      setTasks(tasks.map((t) => (t.id === task.id ? response.data : t)));
    } catch (error) {
      alert("Error updating task: " + error.message);
    }
  };

  const handleEditClick = (task) => {
    setEditingTask(task);
    setEditedDescription(task.description);
  };

  const handleUpdateTask = async (taskId) => {
    if (editedDescription.trim()) {
      try {
        const updatedTask = { ...editingTask, description: editedDescription };
        const response = await API.put(`/task/${taskId}`, updatedTask);
        setTasks(tasks.map((task) => (task.id === taskId ? response.data : task)));
        
        // Reset the editing state after updating
        setEditingTask(null);
        setEditedDescription("");
        fetchTasks();  // Clear the input after the update
      } catch (error) {
        alert("Error updating task: " + error.message);
      }
    }
  };

  const handleKeyDown = (e, taskId) => {
    if (e.key === "Enter") {
      handleUpdateTask(taskId);
    }
  };

  const handleBlur = (taskId) => {
    handleUpdateTask(taskId);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div className="todo-container">
      <header className="todo-header">
        <h1>To-Do List</h1>
        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </header>
      <div className="task-input">
        <input
          type="text"
          placeholder="Add a new task..."
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)}
        />
        <button className="add-btn" onClick={addTask} disabled={!newTask.trim()}>
          Add Task
        </button>
      </div>
      <div className="task-list">
        {tasks.length === 0 ? (
          <p className="no-tasks">No tasks available. Add a task!</p>
        ) : (
          tasks.map((task) => (
            <div
              key={task.id}
              className={`task-item ${task.completed ? "completed" : ""}`}
            >
              {editingTask?.id === task.id ? (
                <input
                  type="text"
                  value={editedDescription}
                  onChange={(e) => setEditedDescription(e.target.value)}
                  onBlur={() => handleBlur(task.id)}  // Save on blur
                  onKeyDown={(e) => handleKeyDown(e, task.id)}  // Save on Enter
                />
              ) : (
                <span
                  className="task-text"
                  onClick={() => toggleTaskCompletion(task)}  // Toggle task completion
                >
                  {task.description}
                </span>
              )}
              <button
                className="delete-btn"
                onClick={() => deleteTask(task.id)}  // Delete task
              >
                Delete
              </button>
              {editingTask?.id !== task.id && (
                <button
                  className="edit-btn"
                  onClick={() => handleEditClick(task)}  // Enable editing mode
                >
                  Edit
                </button>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default TodoList;
