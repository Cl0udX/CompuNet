export const Router = (paths) => {

    const path = window.location.pathname;

    const routeComponent = paths[path] || (() => {
        const notFound = document.createElement('div');
        notFound.textContent = '404 - Page Not Found';
        return notFound;
    });

    return routeComponent();
}