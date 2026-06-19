package com.fitnessapp.service;

import com.fitnessapp.dto.response.WorkoutResponseDTO;
import com.fitnessapp.entity.User;
import com.fitnessapp.entity.Workout;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.mapper.WorkoutMapper;
import com.fitnessapp.repository.ExerciseRepository;
import com.fitnessapp.repository.UserRepository;
import com.fitnessapp.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)//Activa Mockito(clon) para esta clase
class WorkoutServiceTest {

    //CREACIÓN DE LOS MOCKS(Objetos Falsos)
    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    //CUMPLE LA FUNCIÓN DE UNA ESPECIE DE CONSTRUCTOR
    @InjectMocks
    private WorkoutService workoutService;

    //Variables que usaremos en los tests
    private UUID userId;
    private User user;
    private Workout workout;
    private WorkoutResponseDTO workoutResponseDTO;

    @BeforeEach//Se ejecuta ANTES de cada test
    void setUp() {
        //Preparar datos de prueba
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@test.com")
                .build();

        workout = Workout.builder()
                .id(UUID.randomUUID())
                .user(user)
                .name("Push Day")
                .date(LocalDate.now())
                .completed(false)
                .build();

        workoutResponseDTO = WorkoutResponseDTO.builder()
                .id(workout.getId())
                .name("Push Day")
                .date(LocalDate.now())
                .completed(false)
                .build();
    }

    @Test
    @DisplayName("getUserWorkouts - debería retornar lista de workouts del usuario")
    void getUserWorkouts_deberiaRetornarListaDeWorkouts() {
        //ARRANGE(Preparar)
        //Configurar qué debe retornar el mock cuando se llame
        when(workoutRepository.findByUserIdOrderByDateDesc(userId))
                .thenReturn(List.of(workout));

        when(workoutMapper.toWorkoutResponseDTOSimple(workout))
                .thenReturn(workoutResponseDTO);

        //ACT(Actuar)
        //Llamar al método que queremos probar
        List<WorkoutResponseDTO> resultado = workoutService.getUserWorkouts(userId);

        //ASSERT(Verificar)
        //Comprobar que el resultado es el esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Push Day", resultado.get(0).getName());

        //VERIFY(verificar comportamiento)
        //Verificar que los métodos mocks fueron llamados
        verify(workoutRepository, times(1)).findByUserIdOrderByDateDesc(userId);
        verify(workoutMapper, times(1)).toWorkoutResponseDTOSimple(workout);
    }

    @Test
    @DisplayName("getUserWorkouts - debería retornar lista vacía si no hay workouts")
    void getUserWorkouts_deberiaRetornarListaVacia() {
        //ARRANGE
        when(workoutRepository.findByUserIdOrderByDateDesc(userId))
                .thenReturn(List.of()); //Lista vacía

        //ACT
        List<WorkoutResponseDTO> resultado = workoutService.getUserWorkouts(userId);

        //ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(workoutRepository, times(1)).findByUserIdOrderByDateDesc(userId);
        //El mapper no debería ser llamado si no hay workouts
        verify(workoutMapper, never()).toWorkoutResponseDTOSimple(any());
    }

    @Test
    @DisplayName("getWorkoutById - debería retornar workout si pertenece al usuario")
    void getWorkoutById_deberiaRetornarWorkout(){
        //ARRANGE
        UUID workoutId = workout.getId();

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
        when(workoutMapper.toWorkoutResponseDTO(workout)).thenReturn(workoutResponseDTO);

        //ACT
        WorkoutResponseDTO resultado = workoutService.getWorkoutById(workoutId, userId);

        //ASSERT
        assertNotNull(resultado);
        assertEquals("Push Day", resultado.getName());


        verify(workoutRepository, times(1)).findById(workoutId);
    }

    @Test
    @DisplayName("getWorkoutById - debería lanzar excepción si el workout no existe")
    void getWorkoutById_deberiaLanzarExcepcionSiNoExiste(){
        //ARRANGE
        UUID workoutId = UUID.randomUUID();

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {workoutService.getWorkoutById(workoutId, userId);
        });
    }

    @Test
    @DisplayName("deleteWorkout - debería eliminar workout del usuario")
    void deleteWorkout_deberiaEliminarWorkout(){
        //ARRANGE
        UUID workoutId = workout.getId();

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        //ACT
        workoutService.deleteWorkout(workoutId, userId);

        //ASSERT
        verify(workoutRepository, times(1)).delete(workout);
    }

    @Test
    @DisplayName("deleteWorkout - debería lanzar excepción si workout no pertenece al usuario")
    void deleteWorkout_deberiaLanzarExcepcionSiNoEsDelUsuario() {
        //ARRANGE
        UUID workoutId = workout.getId();
        UUID otroUserId = UUID.randomUUID();//Usuario diferente

        when(workoutRepository.findById(workoutId))
                .thenReturn(Optional.of(workout));

        //ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            workoutService.deleteWorkout(workoutId, otroUserId);
        });

        //Verificar que NO se llamó delete
        verify(workoutRepository, never()).delete(any());
    }

}
