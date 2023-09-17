# Spiral - Your Ultimate Media Hub

Spiral is a feature-rich, full-stack web application that provides a comprehensive platform for discovering, organizing, and engaging with various forms of media content. With Spiral, you can search for movies, shows, games, books, and albums, favorite them, write reviews, and interact with the community through upvotes and downvotes on reviews.

## Features

- **Multi-Media Search:** Easily search for movies, TV shows, games, books, and albums within a single platform.
- **User Profiles:** Create and manage your user profile to curate your favorite media items.
- **Review System:** Share your thoughts and opinions by writing reviews for the content you've experienced.
- **Community Interaction:** Engage with the Spiral community by upvoting or downvoting reviews.
- **Security:** Robust authentication and authorization using Spring Security ensure your data is safe.
- **Database Management:** PostgreSQL database with Flyway version control for seamless data management.
- **Containerization:** Docker for easy deployment and scalability.

## Getting Started

### Prerequisites

- Ensure you have [Docker](https://www.docker.com/) installed on your system.
- [Node.js](https://nodejs.org/) and [npm](https://www.npmjs.com/) for running Angular.


### Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/yigitcanyontem/Spiral.git

2. Set Up Angular:
   - Navigate to the frontend directory:
      ```bash
      cd frontend

   - Install dependencies:
      ```bash
      npm install

   - Start the Angular development server:
      ```bash
      ng serve
  
   The Angular app will be accessible at http://localhost:4200.


3. Set Up Spring Boot:
   - Return to the main project directory:
      ```bash
      cd ..


   - Build the Docker containers:
      ```bash
      docker-compose up --build

   The API will be accessible at http://localhost:8080.

4. Go to http://localhost:4200/signup to create an account and start using the app to create a profile for yourself.

## Contributing

Contributions to Spiral are highly encouraged! Whether you want to report a bug, suggest an enhancement, or contribute code, your involvement is valued. To get started:

1. **Reporting Issues:** If you encounter a problem or have a feature request, please open an issue to discuss your ideas.

2. **Pull Requests:** If you'd like to contribute code, fork this repository, make your changes, and create a pull request. We'll review and merge your contributions.

For major changes or feature additions, please consider opening an issue first to facilitate discussion.


## Acknowledgments

Spiral was developed by Yiğit Can Yöntem. I want to express my gratitude to the open-source community and the contributors of the libraries and frameworks that made this project possible.

## Contact

If you have any questions or inquiries, don't hesitate to reach out. You can contact me at yigitcanyontem@outlook.com.

