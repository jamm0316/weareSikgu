import { useState } from 'react';
import {taskApi} from "/src/api/task/taskApi.js";

const useDeleteTask = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const deleteTask = async (taskId) => {
    try {
      setLoading(true);
      setError(null);

      const response = await taskApi.deleteTaskById(taskId);

      return {
        success: true,
        data: response
      };
    } catch (err) {
      setError(err);
      return {
        success: false,
        error: err
      };
    } finally {
      setLoading(false);
    }
  };

  return {
    deleteTask,
    loading,
    error
  };
};

export default useDeleteTask;