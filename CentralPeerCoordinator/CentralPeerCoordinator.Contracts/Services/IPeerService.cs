using CentralPeerCoordinator.Contracts.Dtos;

namespace CentralPeerCoordinator.Contracts.Services;

public interface IPeerService
{
    Task<List<PeerResponseDto>> GetAllAsync();
    Task<PeerResponseDto> GetAsync(Guid id);
    Task<PeerResponseDto> CreateAsync(PeerRequestDto request);
    Task<PeerResponseDto> UpdateAsync(Guid id, PeerRequestDto request);
    Task DeleteAsync(Guid id);
}
