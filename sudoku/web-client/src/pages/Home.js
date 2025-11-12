function Home() {
    const container = document.createElement('div');
    const heading = document.createElement('h1');
    heading.textContent = 'Welcome to Sudoku Home Page';
    container.appendChild(heading);

    return container;
}

export default Home;
