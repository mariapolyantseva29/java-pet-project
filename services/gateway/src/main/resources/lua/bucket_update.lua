local key = KEYS[1]
local currentTime = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2]) or 10
local refillRate = tonumber(ARGV[3]) or 2

local data = redis.call('HMGET', key, 'available_requests', 'update_date')
local availableRequests = tonumber(data[1])
local lastUpdate = tonumber(data[2])

if not availableRequests or not lastUpdate then
    return 0
end

local timeDiff = currentTime - lastUpdate
local added = timeDiff * refillRate
local newAvailable = availableRequests + added

if newAvailable > capacity then
    newAvailable = capacity
end

if newAvailable < 1 then
    redis.call('HSET', key,
        'available_requests', newAvailable,
        'update_date', currentTime
    )
    return 0
end

newAvailable = newAvailable - 1

redis.call('HSET', key,
    'available_requests', newAvailable,
    'update_date', currentTime
)
return 1
