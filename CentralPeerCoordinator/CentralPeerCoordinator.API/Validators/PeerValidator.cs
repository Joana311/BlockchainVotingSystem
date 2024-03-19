using CentralPeerCoordinator.Contracts.Dtos;
using FluentValidation;
using System.Net;

namespace CentralPeerCoordinator.API.Validators;

public class PeerValidator : AbstractValidator<PeerRequestDto>
{
    public PeerValidator()   // TODO check that validation works
    {
        RuleFor(x => x.IpAddress)
            .NotNull().WithMessage("IP address is required.")
            .Must(ValidateIPv4).WithMessage("IP address must be valid.");

        RuleFor(x => x.Port)
            .NotNull().WithMessage("Port is required.")
            .InclusiveBetween(0, 65535).WithMessage("Port must be valid.");
    }

    private static bool ValidateIPv4(string? ipString)
    {
        if (ipString == null) return true;
        if (ipString.Count(c => c == '.') != 3) return false;
        return IPAddress.TryParse(ipString, out _);
    }
}

