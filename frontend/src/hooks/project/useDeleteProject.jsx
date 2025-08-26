import { useState } from 'react';
import {projectApi} from '/src/api/project/projectApi.js';

const useDeleteProject = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const deleteProject = async (projectId) => {
    try {
      setLoading(true);
      setError(null);

      const response = await projectApi.deleteProjectById(projectId);

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
    deleteProject,
    loading,
    error
  };
};

export default useDeleteProject;