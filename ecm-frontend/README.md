# ECM System - Angular Frontend

A modern, responsive Angular application for the Enterprise Case Management (ECM) system.

## Features

- **Modern UI/UX**: Built with Angular Material Design components
- **Authentication**: JWT-based authentication with login/logout functionality
- **Dashboard**: Overview with statistics and progress tracking
- **Case Management**: View, create, and manage cases
- **User Management**: Admin interface for user management
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Real-time Updates**: Live data updates from the backend API

## Prerequisites

- Node.js (v20.10.0 or higher)
- npm (v10.2.3 or higher)
- Angular CLI (v17.3.17)

## Installation

1. Navigate to the frontend directory:
   ```bash
   cd ecm-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Open your browser and navigate to `http://localhost:4200`

## Development

### Project Structure

```
src/
├── app/
│   ├── auth/                 # Authentication components
│   │   └── login/
│   ├── cases/               # Case management components
│   │   ├── case-list/
│   │   ├── case-form/
│   │   └── case-detail/
│   ├── dashboard/           # Dashboard component
│   ├── layout/              # Main layout component
│   ├── users/               # User management components
│   ├── profile/             # User profile component
│   ├── guards/              # Route guards
│   ├── interceptors/        # HTTP interceptors
│   ├── models/              # TypeScript interfaces
│   ├── services/            # API services
│   └── shared/              # Shared modules and components
├── environments/            # Environment configurations
└── assets/                  # Static assets
```

### Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm run build:dev` - Build for development
- `npm test` - Run unit tests
- `npm run lint` - Run linting

### Environment Configuration

The application uses environment-specific configurations:

- **Development**: `src/environments/environment.ts`
- **Production**: `src/environments/environment.production.ts`

Update the `apiUrl` in these files to match your backend API endpoint.

### API Integration

The frontend communicates with the Spring Boot backend through RESTful APIs:

- **Authentication**: `/api/auth/*`
- **Cases**: `/api/cases/*`
- **Users**: `/api/users/*`

All API calls include JWT authentication tokens automatically.

## Features in Detail

### Authentication
- Login form with validation
- JWT token management
- Automatic token refresh
- Route protection with guards

### Dashboard
- Case statistics overview
- User activity metrics
- Progress tracking
- Quick action buttons

### Case Management
- List all cases with filtering
- Create new cases
- View case details
- Update case status
- Assign cases to users

### User Management (Admin Only)
- List all users
- Create new users
- Update user information
- Activate/deactivate users

## Styling

The application uses:
- **Angular Material**: UI components and theming
- **SCSS**: Custom styling with variables
- **Responsive Design**: Mobile-first approach
- **Modern CSS**: Grid, Flexbox, and CSS custom properties

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Deployment

### Development
```bash
npm start
```

### Production
```bash
npm run build
```

The built files will be in the `dist/` directory, ready for deployment to any static hosting service.

## Contributing

1. Follow the existing code style
2. Write unit tests for new features
3. Update documentation as needed
4. Use meaningful commit messages

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure the backend is configured to allow requests from `http://localhost:4200`
2. **Authentication Issues**: Check that the JWT token is being sent correctly
3. **Build Errors**: Clear the `node_modules` and reinstall dependencies

### Getting Help

- Check the browser console for error messages
- Verify the backend API is running and accessible
- Ensure all environment variables are set correctly

## License

This project is part of the ECM System and follows the same licensing terms.
