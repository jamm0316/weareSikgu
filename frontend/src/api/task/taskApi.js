import {ENDPOINTS} from "./endpoints.js";
import apiClient from "/src/api/client.js";

export const taskApi = {
  createTask: async (projectData) => {
    const response = await apiClient.post(ENDPOINTS.TASKS, projectData);
    return response.data;
  },

  summaryTask: async () => {
    const response = await apiClient.get(ENDPOINTS.SUMMARY_TASKS);
    return response.data.result;
  },

  todayTask: async () => {
    const response = await apiClient.get(ENDPOINTS.TODAY_TASKS);
    return response.data.result;
  },

  getTaskById: async (id) => {
    const response = await apiClient.get(ENDPOINTS.TASK_BY_ID(id));
    return response.data.result;
  },

  updateTaskFieldById: async (id, taskData) => {
    const response = await apiClient.patch(ENDPOINTS.UPDATE_TASK_FIELD_BY_ID(id), taskData);
    return response.data.result;
  },

  deleteTaskById: async (id) => {
    const response = await apiClient.delete(ENDPOINTS.TASK_BY_ID(id));
    return response.data.result;
  }
}