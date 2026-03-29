import React, { useState, useEffect } from 'react';
import api from '../services/api';
import Layout from '../components/Layout';

const Dashboard = () => {
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    const { data } = await api.get('/tasks');
    setTasks(data.content);
  };

  return (
    <Layout>
      <div className="container mt-4">
        <h2>My Tasks</h2>
        <div className="row">
          {tasks.map(task => (
            <div className="col-md-4 mb-3" key={task.id}>
              <div className="card">
                <div className="card-body">
                  <h5>{task.title}</h5>
                  <p>{task.description}</p>
                  <span className={`badge bg-${task.status === 'COMPLETED' ? 'success' : 'warning'}`}>
                    {task.status}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;