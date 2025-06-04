# Agodop
> Agodop is a simple Android weather app.

On the main screen, it shows:
  - Temperature
  - Humidity
  - Cloud coverage
  - Rain in mm

If you tap on any of these, it opens a new screen with data for the whole week for that specific item.
You can press the back button to return to the main screen.

## Rooting
flowchart TD
    Start --> CheckDeviceCompatibility
    CheckDeviceCompatibility --> UnlockBootloader
    UnlockBootloader --> InstallCustomRecovery
    InstallCustomRecovery --> FlashRootPackage
    FlashRootPackage --> RebootDevice
    RebootDevice --> VerifyRootAccess
    VerifyRootAccess --> End

    Start([Start])
    CheckDeviceCompatibility([Check Device Compatibility])
    UnlockBootloader([Unlock Bootloader])
    InstallCustomRecovery([Install Custom Recovery])
    FlashRootPackage([Flash Root Package (e.g., Magisk)])
    RebootDevice([Reboot Device])
    VerifyRootAccess([Verify Root Access])
    End([End])

## Additional info
Most of the project was done on discord call, so changes were commited by one person
