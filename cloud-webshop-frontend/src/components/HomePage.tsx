import React from 'react';
import { Typography, Container, Button, Box } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const HomePage: React.FC = () => {
  // A mock user ID for demonstration purposes
  const mockUserId = '123e4567-e89b-12d3-a456-426614174000';

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Welcome to the Cloud Webshop!
      </Typography>
      <Typography variant="body1" paragraph>
        This is the main landing page. From here, you can navigate to different parts of the application.
      </Typography>
      <Box sx={{ mt: 4 }}>
        <Button
          variant="contained"
          color="primary"
          component={RouterLink}
          to={`/users/${mockUserId}`}
          sx={{ mr: 2 }}
        >
          View Your Profile
        </Button>
        <Button
          variant="contained"
          color="secondary"
          component={RouterLink}
          to={`/cart/${mockUserId}`}
        >
          Go to Your Cart
        </Button>
      </Box>
    </Container>
  );
};

export default HomePage;
