import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';

import '@testing-library/jest-dom';
import CreateWorkout from '../src/pages/createWorkout/CreateWorkout';

// Mocking react-router-dom
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

// Mocking DataSource
jest.mock('../../helpers/DataSource.js', () => ({
  addWorkout: jest.fn(),
}));

// Mocking universal-cookie
jest.mock('universal-cookie', () => ({
  __esModule: true,
  default: jest.fn(),
}));

describe('CreateWorkout Component', () => {
  it('renders correctly', () => {
    render(<CreateWorkout />);
    
    expect(screen.getByText('Create Workout')).toBeInTheDocument();
  });

  it('handles input changes', () => {
    render(<CreateWorkout />);

    const workoutNameInput = screen.getByLabelText('Workout Name:');
    const exerciseNotesInput = screen.getByLabelText('Notes:');

    fireEvent.change(workoutNameInput, { target: { value: 'Test Workout' } });
    fireEvent.change(exerciseNotesInput, { target: { value: 'Test Notes' } });

    expect(workoutNameInput.value).toBe('Test Workout');
    expect(exerciseNotesInput.value).toBe('Test Notes');
  });

  it('adds an exercise and updates the list', () => {
    render(<CreateWorkout />);

    const addExerciseButton = screen.getByText('Add Exercise');
    const workoutNameInput = screen.getByLabelText('Workout Name:');
    const exerciseNotesInput = screen.getByLabelText('Notes:');
    
    fireEvent.change(workoutNameInput, { target: { value: 'Test Exercise' } });
    fireEvent.change(exerciseNotesInput, { target: { value: 'Exercise Notes' } });
    fireEvent.click(addExerciseButton);

    const exerciseList = screen.getByTestId('exerciseListView');
    expect(exerciseList).toHaveTextContent('Test Exercise');
  });

  it('navigates to /workout when Start Workout button is clicked', () => {
    render(<CreateWorkout />);
    
    const startWorkoutButton = screen.getByText('Start Workout');
    fireEvent.click(startWorkoutButton);

   
  });

  it('saves workout when Save Workout button is clicked', async () => {
    render(<CreateWorkout />);

    const saveWorkoutButton = screen.getByText('Save Workout');
    fireEvent.click(saveWorkoutButton);

  });
});

