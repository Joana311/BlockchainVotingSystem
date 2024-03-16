using AutoMapper;
using CentralPeerCoordinator.Contracts.Dtos;
using CentralPeerCoordinator.Contracts.Entities;

namespace CentralPeerCoordinator.Common.Profiles;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<PeerRequestDto, Peer>();

        CreateMap<Peer, PeerResponseDto>();
    }
}
