/**
 * @typedef {Object} RouteConfig
 * @property {string} name - The name of the state.
 * @property {string} url - The URL associated with the state.
 * @property {string|function(any):string} serverPath - The server path associated with the state.
 */

/**
 * Generate an array of default CRUD controller route configurations for a given route name.
 *
 * @param {string} routeName - The base name of the route.
 * @returns {RouteConfig[]} An array of default route configurations.
 */
export default function generateRouteConfig(routeName) {
  const baseServerPath = `/_${routeName}`;

  const routes = [
    { name: routeName, url: `/${routeName}`, serverPath: baseServerPath },
    {
      name: `${routeName}:get`,
      url: `/${routeName}/:id`,
      serverPath: (param) => `${baseServerPath}/${param.id}`,
    },
    {
      name: `${routeName}:new`,
      url: `/${routeName}/new`,
      serverPath: `${baseServerPath}/new`,
    },
    {
      name: `${routeName}:edit`,
      url: `/${routeName}/edit/:id`,
      serverPath: (param) => `${baseServerPath}/edit/${param.id}`,
    },
  ];

  return routes;
}
