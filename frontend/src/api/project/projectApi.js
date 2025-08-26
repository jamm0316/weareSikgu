import {ENDPOINTS} from "./endpoints.js";
import apiClient from "/src/api/client.js";

export const projectApi = {
  createProject: async (projectData) => {
    const response = await apiClient.post(ENDPOINTS.PROJECT, projectData);
    return response.data;
  },
  
  summaryProject: async () => {
    const response = await apiClient.get(ENDPOINTS.SUMMARY_PROJECTS);
    return response.data.result;
  },

  getProjectById: async (id) => {
    const response = await apiClient.get(ENDPOINTS.PROJECT_BY_ID(id));
    return response.data.result;
  },

  updateProjectFieldById: async (id, projectData) => {
    const response = await apiClient.patch(ENDPOINTS.UPDATE_PROJECT_FIELD_BY_ID(id), projectData);
    return response.data.result;
  },

  searchProject: async (search) => {
    const response  = await apiClient.get(ENDPOINTS.SEARCH_PROJECTS(search));
    return response.data.result;
  },

  deleteProjectById: async (id) => {
    const response = await apiClient.delete(ENDPOINTS.PROJECT_BY_ID(id));
    return response.data.result;
  }
}