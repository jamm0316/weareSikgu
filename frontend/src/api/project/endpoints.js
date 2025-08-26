const VERSION = 'v1';
const DOMAIN = 'project'
export const ENDPOINTS = {
  PROJECT: `${VERSION}/${DOMAIN}`,
  SUMMARY_PROJECTS: `${VERSION}/${DOMAIN}/summary`,
  PROJECT_BY_ID: (id) => `${VERSION}/${DOMAIN}/${id}`,
  UPDATE_PROJECT_FIELD_BY_ID: (id) => `${VERSION}/${DOMAIN}/${id}/field`,
  SEARCH_PROJECTS: (q) =>
    `${VERSION}/${DOMAIN}/search?keyword=${encodeURIComponent(q)}`,
}