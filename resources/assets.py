
from mcresources import ResourceManager


def generate(rm: ResourceManager):

    # First
    rm.lang({
        'itemGroup.notreepunching.items': 'No Tree Punching'
    })

    # Stone
    for stone in ('granite', 'andesite', 'diorite'):
        rm.blockstate('%s_cobblestone' % stone) \
            .with_block_model() \
            .with_item_model() \
            .with_tag('cobblestone') \
            .with_block_loot('notreepunching:%s_cobblestone' % stone) \
            .with_lang(lang('%s cobblestone', stone)) \
            .make_stairs() \
            .make_slab() \
            .make_wall()
        for piece in ('stairs', 'slab', 'wall'):
            rm.block('%s_cobblestone_%s' % (stone, piece)) \
                .with_lang(lang('%s cobblestone %s', stone, piece)) \
                .with_tag(piece + ('s' if not piece.endswith('s') else ''))  # plural tag

    for stone in ('granite', 'andesite', 'diorite', 'stone', 'sandstone', 'red_sandstone'):
        rm.blockstate('%s_loose_rock' % stone) \
            .with_block_model(textures='minecraft:%s' % stone, parent='notreepunching:block/loose_rock') \
            .with_block_loot('notreepunching:%s_loose_rock' % stone) \
            .with_lang(lang('%s loose rock', stone))
        # flat item model for the block item
        rm.item_model('%s_loose_rock' % stone)

    # Pottery
    for pottery in ('worked', 'large_vessel', 'small_vessel', 'bucket', 'flower_pot'):
        block = rm.blockstate('clay_%s' % pottery) \
            .with_block_model(textures='minecraft:clay', parent='notreepunching:block/pottery_%s' % pottery) \
            .with_item_model() \
            .with_block_loot('notreepunching:clay_%s' % pottery)
        if pottery == 'worked':
            block.with_lang(lang('worked clay'))
        else:
            block.with_lang(lang('clay %s', pottery))

    rm.blockstate('ceramic_large_vessel') \
        .with_block_model(textures='notreepunching:block/ceramic', parent='notreepunching:block/pottery_large_vessel') \
        .with_item_model() \
        .with_block_loot('notreepunching:ceramic_large_vessel') \
        .with_lang(lang('ceramic large vessel'))

    # Tools
    for tool in ('iron', 'gold', 'diamond'):
        rm.item_model('%s_mattock' % tool) \
            .with_lang(lang('%s mattock', tool))
        rm.item_model('%s_saw' % tool) \
            .with_lang(lang('%s saw', tool)) \
            .with_tag('saws')
        rm.item_model('%s_knife' % tool) \
            .with_lang(lang('%s knife', tool))

    # Flint
    for tool in ('axe', 'pickaxe', 'shovel', 'hoe', 'knife'):
        rm.item_model('flint_%s' % tool) \
            .with_lang(lang('flint %s', tool))
    rm.item_model('macuahuitl') \
        .with_lang(lang('macuahuitl'))

    for item in ('flint_shard', 'plant_fiber', 'plant_string', 'clay_brick', 'ceramic_small_vessel', 'clay_tool', 'fire_starter'):
        rm.item_model(item) \
            .with_lang(lang(item))


    # todo: ceramic bucket


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
