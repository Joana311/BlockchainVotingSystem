using AutoMapper;
using CentralPeerCoordinator.Contracts.Dtos;
using CentralPeerCoordinator.Contracts.Entities;
using CentralPeerCoordinator.Contracts.Exceptions;
using CentralPeerCoordinator.Contracts.Services;
using CentralPeerCoordinator.Contracts.UoW;

namespace CentralPeerCoordinator.Services;

public class PeerService : IPeerService
{
    private readonly IUnitOfWork _uow;
    private readonly IMapper _mapper;

    public PeerService(IUnitOfWork uow, IMapper mapper)
    {
        _uow = uow;
        _mapper = mapper;
    }

    public async Task<List<PeerResponseDto>> GetAllAsync()
    {
        List<Peer> peers = await _uow.Peers.GetAllAsync();
        return peers.Select(p => _mapper.Map<PeerResponseDto>(p)).ToList();
    }

    public async Task<PeerResponseDto> GetAsync(Guid id)
    {
        Peer peer = await _uow.Peers.GetAsync(id) ??
            throw new EntityNotFoundException("There is no mission with that id.");
        return _mapper.Map<PeerResponseDto>(peer);
    }

    public async Task<PeerResponseDto> UpdateAsync(Guid id, PeerRequestDto request)
    {
        Peer peer = await _uow.Peers.GetAsync(id) ??
            throw new EntityNotFoundException("There is no peer with that id.");
        peer = _mapper.Map(request, peer);
        _uow.Peers.Update(peer);
        await _uow.SaveChangesAsync();
        return _mapper.Map<PeerResponseDto>(peer);
    }

    public async Task<PeerResponseDto> CreateAsync(PeerRequestDto request)
    {
        Peer peer = _mapper.Map<Peer>(request);
        _uow.Peers.Add(peer);
        await _uow.SaveChangesAsync();
        return _mapper.Map<PeerResponseDto>(peer);
    }

    public async Task DeleteAsync(Guid id)
    {
        Peer peer = await _uow.Peers.GetAsync(id) ??
            throw new EntityNotFoundException("There is no peer with that id.");
        _uow.Peers.Remove(peer);
        await _uow.SaveChangesAsync();
    }
}
