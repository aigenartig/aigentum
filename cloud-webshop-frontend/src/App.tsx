import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import UserProfile from './components/UserProfile';
import ShoppingCart from './components/ShoppingCart';
import HomePage from './components/HomePage';
import { Container, Typography } from '@mui/material';

function App() {
  return (
    <Router>
      <Container>
        <Typography variant="h3" component="h1" gutterBottom>
          Cloud Webshop
        </Typography>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/api/v1/users/:userId" element={<UserProfile />} />
          <Route path="/cart/:userId" element={<ShoppingCart />} />
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
